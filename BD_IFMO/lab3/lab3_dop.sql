

CREATE TYPE status as enum(
    'Активен',
    'Угасает',
    'Потух'
    );
CREATE TYPE weathers as enum(
            'Солнечная',
            'Ветряная',
            'Дождливая',
            'Туманная');

CREATE TABLE planets(
    id INT GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    size INT,
    type_planet_id INT NOT NULL ,
    world_id INT NOT NULL ,
    CONSTRAINT pk_planets PRIMARY KEY (id),
    CONSTRAINT uq_planets_name UNIQUE (name)
);

CREATE TABLE places (
    id INT GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    coordinates POINT,
    planet_id INT NOT NULL,
    weather weathers NOT NULL,
    CONSTRAINT pk_places PRIMARY KEY (id),
    CONSTRAINT fk_places_planet_id FOREIGN KEY (planet_id) REFERENCES planets(id)

);

CREATE TABLE lights (
    id INT GENERATED ALWAYS AS IDENTITY,
    place_id INT NOT NULL ,
    status STATUS NOT NULL DEFAULT 'Активен',
    CONSTRAINT pk_lights PRIMARY KEY (id),
    CONSTRAINT fk_lights_place_id FOREIGN KEY (place_id) REFERENCES places(id)
);

CREATE TABLE debris(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(30),
    size FLOAT,
    weight FLOAT,
    planet_id INT NOT NULL ,
    discoverer_id INT NOT NULL,
    CONSTRAINT pk_debris PRIMARY KEY (id),
    CONSTRAINT fk_debris_planets FOREIGN KEY (planet_id) REFERENCES planets(id)
);

CREATE OR REPLACE FUNCTION size_debris_in_size_planets()
RETURNS TRIGGER
AS
$$
DECLARE
    size_debris_int INT;
    new_planet_size INT;
    old_planet_size INT;
BEGIN
    IF tg_op = 'DELETE' THEN
        size_debris_int := OLD.size::INT;
        UPDATE planets
        SET size = size-size_debris_int
        WHERE id=OLD.planet_id;
        RETURN OLD;

    ELSIF tg_op = 'INSERT' THEN
        size_debris_int := NEW.size::INT;

        UPDATE planets
        SET size= size + size_debris_int
        WHERE id=NEW.planet_id
        RETURNING size INTO new_planet_size;
        old_planet_size:= new_planet_size - size_debris_int;

        IF old_planet_size <675434 AND new_planet_size >= 675434 THEN
            UPDATE places
            SET weather = 'Туманная'
            WHERE planet_id= NEW.planet_id;

            UPDATE lights
            SET status = 'Угасает'
            WHERE place_id IN (
                SELECT id
                FROM places
                WHERE planet_id=NEW.planet_id
                );
        END IF;

        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.size=NEW.size AND OLD.planet_id=NEW.planet_id THEN
            RETURN NEW;
        end if;

        IF OLD.planet_id = NEW.planet_id THEN
            UPDATE planets
            SET size = size + (NEW.size::INT - OLD.size::INT)
            WHERE id = NEW.planet_id
            RETURNING size INTO new_planet_size;
            old_planet_size := new_planet_size - (NEW.size::INT - OLD.size::INT);
        ELSE
            UPDATE planets SET size = size - OLD.size::INT
            WHERE id = OLD.planet_id;

            UPDATE planets
            SET size = size +NEW.size::INT
            WHERE id = NEW.planet_id
            RETURNING size INTO new_planet_size;
            old_planet_size := new_planet_size - NEW.size::INT;

        end if;
        IF old_planet_size <675434 AND new_planet_size >= 675434 THEN
            UPDATE places
            SET weather = 'Туманная'
            WHERE planet_id= NEW.planet_id;

            UPDATE lights
            SET status = 'Угасает'
            WHERE place_id IN (
                SELECT id
                FROM places
                WHERE planet_id=NEW.planet_id
                );
        END IF;
        RETURN NEW;

    END IF;
END;

$$
LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_size_debris_in_size_planets
AFTER INSERT OR DELETE OR UPDATE ON debris
FOR EACH ROW
EXECUTE FUNCTION size_debris_in_size_planets();


-- обычная вью
CREATE OR REPLACE VIEW v_planet_analize AS
SELECT pln.id, pln.size cur_planet_size,
       COUNT(DISTINCT pls.id) total_places,
       COUNT(DISTINCT lghs.id) total_lights,
       COUNT (DISTINCT CASE
       WHEN lghs.status = 'Угасает' THEN lghs.id END) fadeable_lights,
        count(DISTINCT dbrs.id) total_debris,
        COALESCE( AVG(dbrs.size), 0 ) avg_debris_size

FROM planets pln
LEFT JOIN places pls ON pln.id = pls.planet_id
LEFT JOIN lights lghs ON pls.id = lghs.place_id
LEFT JOIN debris dbrs on pln.id = dbrs.planet_id
GROUP BY pln.id, pln.size;

CREATE MATERIALIZED VIEW mat_v_planet_analize AS
SELECT pln.id, pln.size cur_planet_size,
       COUNT(DISTINCT pls.id) total_places,
       COUNT(DISTINCT lghs.id) total_lights,
       COUNT (DISTINCT CASE
       WHEN lghs.status = 'Угасает' THEN lghs.id END) fadeable_lights,
        count(DISTINCT dbrs.id) total_debris,
        COALESCE( AVG(dbrs.size), 0 ) avg_debris_size

FROM planets pln
LEFT JOIN places pls ON pln.id = pls.planet_id
LEFT JOIN lights lghs ON pls.id = lghs.place_id
LEFT JOIN debris dbrs on pln.id = dbrs.planet_id
GROUP BY pln.id, pln.size;

CREATE OR REPLACE FUNCTION refresh_mat_v_planet_analize()
RETURNS TRIGGER
AS
$$
BEGIN
    REFRESH MATERIALIZED VIEW mat_v_planet_analize;
    RETURN NULL;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_refresh_mat_v_planet_analize
AFTER INSERT OR UPDATE OR DELETE ON debris
FOR EACH STATEMENT
EXECUTE FUNCTION refresh_mat_v_planet_analize();

INSERT INTO planets (name, size, type_planet_id, world_id) VALUES
('Земля', 234_344, 1, 1),
('Марс', 7586216, 2, 1),
('Лэш', 122412, 3, 2);

INSERT INTO places (name, coordinates, planet_id, weather) VALUES
('Нью', '(40.7, 32.2)', 1, 'Дождливая'),
('Высокий ник', '(40.7, 32.2)', 1, 'Солнечная'),
('Трик', '(40.7, 32.2)', 2, 'Ветряная'),
('Лэш-сити', '(40.7, 32.2)', 3, 'Туманная');

INSERT INTO lights(place_id, status) VALUES
(1,'Активен'),
(1,'Потух'),
(3,'Активен'),
(2,'Активен'),
(4,'Угасает'),
(4,'Активен');

INSERT INTO debris(name, size, weight, planet_id, discoverer_id) VALUES
('Скин',42323, 2234, 1, 1),
('Дракс',4223, 2234, 2, 1),
('Бритиш',232, 2234, 3, 1),
('Довс',86572, 2234, 3, 1);



INSERT INTO debris(name, size, weight, planet_id, discoverer_id) SELECT
'Обломок - ' || i,
random()*100000,
random()*5000 +10,
(random() *2+1)::INT,
1
FROM generate_series(1, 10000000) i;

SELECT pg_size_pretty(pg_total_relation_size('debris')), COUNT(*) FROM debris;



--docker exec pg-local pgbench -U rusik2k -d rusik2k -c 1 -t 1 -f /read_v.sql просто вызываю все записи вьюшки, долго, что то на ровне с записью и срабатыавением тригера
--docker exec pg-local pgbench -U rusik2k -d rusik2k -c 1 -t 1 -f /read_mat_v.sql не рефрешу перед вызовом вью, ну типо нет смысла так как берутся старые данные
--docker exec pg-local pgbench -U rusik2k -d rusik2k -c 1 -t 1 -f /write_test_debr.sql просто вставляю новый обломок и срабатывает два тригера по сути
--docker exec pg-local pgbench -U rusik2k -d rusik2k -c 1 -t 1 -f /read_mater_v2.sql обнвляю перед вызовом матер вью по времени так же как и просто вью