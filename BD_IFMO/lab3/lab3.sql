/*color VARCHAR (27) GENERATED ALWAYS AS (
        CASE
            WHEN type_planet_id=1 or type_planet_id=2 THEN 'Красный'
            WHEN type_planet_id=3 THEN 'Холодный'
            ELSE 'Неизвестный цвет'
        END
        ) STORED */

/*ALTER TABLE type_lights DROP COLUMN color;

ALTER TABLE type_lights ADD COLUMN color TEXT;*/

--авто вставление данных в цвет типа огня
CREATE OR REPLACE FUNCTION set_type_lights_color()
RETURNS TRIGGER
AS $$
    BEGIN
        NEW.color := CASE
            WHEN NEW.type_planet_id IN(1,2) THEN 'Красный'
            WHEN NEW.type_planet_id=3 THEN 'Холодный'
            ELSE 'Неизвестный цвет'
            END;
        RETURN NEW;
    END;
    $$
LANGUAGE plpgsql;

CREATE TRIGGER trg_set_type_lights_color
BEFORE INSERT OR UPDATE
ON type_lights
FOR EACH ROW
EXECUTE FUNCTION set_type_lights_color();

/*INSERT INTO type_lights (feature, type_planet_id) VALUES
        ('Пламенные вихри', 1),
        ('Беспорядочные вспышки', 2),
        ('Призрачное сияние', 3);*/
-- INSERT INTO type_lights (feature, type_planet_id, color) VALUES ('fsfs',1, 'Холодный');
-- DELETE FROM type_lights where id=4;
--
-- SELECT setval(pg_get_serial_sequence('type_lights','id'),
--        COALESCE(MAX(id),0)
--        ) FROM type_lights;

-- размер планеты от добавления или удаления осколка
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
            SET weather = 4
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
            SET weather = 4
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


INSERT INTO debris(name, size, weight, planet_id, discoverer_id)
VALUES ('Куш-пак', 200_000,879322, 4, 3);
DELETE FROM debris WHERE id=7;

UPDATE debris set size=800_000 where id=8;
