
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

CREATE TABLE type_weathers(
    id INT GENERATED ALWAYS AS IDENTITY CONSTRAINT pk_type_weathers PRIMARY KEY ,
    type WEATHERS NOT NULL

);

CREATE TABLE type_planets (
    id INT GENERATED ALWAYS AS IDENTITY,
    type VARCHAR(20) NOT NULL,
    CONSTRAINT pk_type_planets PRIMARY KEY (id)
);

CREATE TABLE worlds (
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(40) NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT 'Внутренний',
    galaxy VARCHAR(50),
    CONSTRAINT pk_worlds PRIMARY KEY (id)
);

CREATE TABLE planets(
    id INT GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    size INT,
    type_planet_id INT NOT NULL ,
    world_id INT NOT NULL ,
    CONSTRAINT pk_planets PRIMARY KEY (id),
    CONSTRAINT uq_planets_name UNIQUE (name),
    CONSTRAINT fk_planets_type_planet_id FOREIGN KEY (type_planet_id) REFERENCES type_planets(id),
    CONSTRAINT fk_planets_worlds FOREIGN KEY (world_id) REFERENCES worlds(id)
);

CREATE TABLE places (
    id INT GENERATED ALWAYS AS IDENTITY,
    name TEXT NOT NULL,
    coordinates POINT,
    planet_id INT NOT NULL,
    weather INT NOT NULL,
    CONSTRAINT pk_places PRIMARY KEY (id),
    CONSTRAINT fk_places_planet_id FOREIGN KEY (planet_id) REFERENCES planets(id),
    CONSTRAINT fk_places_type_weathers FOREIGN KEY (weather) REFERENCES type_weathers(id)
);

CREATE TABLE type_lights (
    id INT GENERATED ALWAYS AS IDENTITY,
    feature TEXT,
    type_planet_id INT NOT NULL,
    color VARCHAR (27) GENERATED ALWAYS AS (
        CASE
            WHEN type_planet_id=1 or type_planet_id=2 THEN 'Красный'
            WHEN type_planet_id=3 THEN 'Холодный'
            ELSE 'Неизвестный цвет'
        END
        ) STORED,
    CONSTRAINT pk_type_lights PRIMARY KEY (id),
    CONSTRAINT fk_type_lights_type_planet_id FOREIGN KEY (type_planet_id) REFERENCES type_planets(id)
);


CREATE TABLE lights (
    id INT GENERATED ALWAYS AS IDENTITY,
    place_id INT NOT NULL ,
    status STATUS NOT NULL DEFAULT 'Активен',
    CONSTRAINT pk_lights PRIMARY KEY (id),
    CONSTRAINT fk_lights_place_id FOREIGN KEY (place_id) REFERENCES places(id)
);

CREATE TABLE fanatics(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(30),
    preference VARCHAR(40),
    height FLOAT,
    sex VARCHAR(20) DEFAULT 'Мужской',
    enemy INT,
    CONSTRAINT pk_fanatics PRIMARY KEY (id),
    CONSTRAINT fk_fanatics_fanatics FOREIGN KEY (enemy) REFERENCES fanatics(id)
);

CREATE TABLE theories (
    id INT GENERATED ALWAYS AS IDENTITY ,
    creator INT NOT NULL ,
    title VARCHAR(50),
    description TEXT,
    date DATE NOT NULL,
    CONSTRAINT pk_theory PRIMARY KEY (id),
    CONSTRAINT fk_theory_fanatics FOREIGN KEY (creator) REFERENCES fanatics(id)
);

CREATE TABLE residents (
    id_fanatic INT NOT NULL ,
    id_planet INT NOT NULL ,
    CONSTRAINT fk_residents_fanatics FOREIGN KEY (id_fanatic) REFERENCES fanatics(id),
    CONSTRAINT fk_residents_planets FOREIGN KEY (id_planet) REFERENCES planets(id),
    CONSTRAINT pk_residents PRIMARY KEY (id_fanatic, id_planet)
);

CREATE TABLE debris(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(30),
    size FLOAT,
    weight FLOAT,
    planet_id INT NOT NULL ,
    discoverer_id INT NOT NULL,
    CONSTRAINT pk_debris PRIMARY KEY (id),
    CONSTRAINT fk_debris_planets FOREIGN KEY (planet_id) REFERENCES planets(id),
    CONSTRAINT fk_debris_discoverer FOREIGN KEY (discoverer_id) REFERENCES fanatics(id)
);




INSERT INTO type_weathers (type) VALUES
            ('Солнечная'),
            ('Ветряная'),
            ('Дождливая'),
            ('Туманная');

INSERT INTO type_planets (type) VALUES
            ('Огненая'),
            ('Каменистая'),
            ('Ледяная');

INSERT INTO type_lights (feature, type_planet_id) VALUES
        ('Пламенные вихри', 1),
        ('Беспорядочные вспышки', 2),
        ('Призрачное сияние', 3);

INSERT INTO worlds (name,type, galaxy) VALUES
         ('Ледилэнд',DEFAULT,'Молочный путь'),
         ('Триллер','Внешний','Веселый молочник');

INSERT INTO planets (name, size, type_planet_id, world_id) VALUES
        ('Фурион', 157_080, 2, 1),
        ('Лич', 36228, 3, 1),
        ('Хускар', 116_000, 1, 2),
        ('Лэш', 321_422,2, 1),
        ('Бубба', 474774,1, 2);

INSERT INTO places (name, coordinates, planet_id, weather) VALUES
        ('Лесочек','(23.2,23.3)',1,1),
        ('Структуры тишины','(111.1 , 0.29)',2,2),
        ('Радужный мост','(35.38, 139.45)',3,4),
        ('Ночное царство','(666.333, 333.666)',3,3),
        ('Альтернативная ловушка','(21.3, 3.6)',4,1),
        ('Шака','(0.0, 0.0)',5,4),
        ('Подвал','(0.0, -2.0)',5,4);

INSERT INTO lights (place_id, status) VALUES
        (1,DEFAULT),
        (2, 'Потух'),
        (3, 'Угасает'),
        (4, DEFAULT);

INSERT INTO fanatics(name, preference, height, sex, enemy) VALUES
        ('Лорен','Математика', 2.1,'Женский', null),
        ('Майкл','Анатомия', 3.,DEFAULT, 1),
        ('Бибоп','Физика', 0.9,DEFAULT, null),
        ('Хэйз','Космические тела', 1.9,'Женский', 1),
        ('Венатор','Математика', 4.1,DEFAULT, 3);

INSERT INTO residents(id_fanatic, id_planet) VALUES
        (1,2),
        (2,3),
        (3,1),
        (4,4),
        (5,4),
        (4,5);

INSERT INTO debris (name, size, weight, planet_id, discoverer_id) VALUES
            ('Мага', 89.947, 4000.548, 1,3),
            ('Икстэл',111., 2153.2,1,3),
            ('Блич',28.1, 117, 4, 4),
            ('Липтон',5476.4235, 9765843.23454,5,4),
            ('Калифорникейшен',576.235, 783.2354,2,1);


INSERT INTO theories(creator, title, description, date) VALUES
            (1,'Теория большого взрыва', 'Около 13,8 млрд лет назад вся материя, энергия, пространство и даже время существовали в виде чрезвычайно плотного и горячего состояния. Затем произошел стремительный процесс расширения', '1998-02-12'),
            (2,'Теория голографический принцип ', 'Все процессы в нашей вроде как трехмерной Вселенной — проекция происходящего в двумерном мире на некую поверхность. А мы живем внутри голограммы.', '1998-02-21'),
            (3,'Теория великий фильтр', 'В развитии каждой цивилизации существует фактор, который не дает ей развиться до того уровня, на котором она заявляет о себе и может быть замечена другими.', '0118-01-10'),
            (4,'Теория квантовое самоубийство', 'Представьте себе, что на вас направлено ружье. Оно заряжено, и может выстрелить или не выстрелить — с вероятностью 50%, и с равной вероятностью ружье может или убить вас, или так и держать в неопределенности дальше. Согласно многомировой интерпретации квантовой механики Эверетта, после каждого выстрела вселенная расщепляется надвое: в одной вы умираете, а в другой нет. При этом вы сможете вспомнить эксперимент только в той вселенной, где выживаете, поскольку в другой вселенной вы перестаёте существовать.', '02.12.1928'),
            (5,'Теория Солипсизм', 'Не существует ничего кроме индивидуального сознания.', '1998-02-12');


UPDATE fanatics
SET enemy=5 WHERE id=1;

-- вывожу типы планет
SELECT pln.name, tp.type
FROM planets pln JOIN type_planets tp ON pln.type_planet_id= tp.id
WHERE tp.type='Ледяная' and pln.size >1000;
-- достаю плэйс, у которых есть хотя бы один горящий огонь
SELECT plc.name--, lts.status
FROM places plc JOIN lights lts ON plc.id=lts.place_id
GROUP BY plc.name
HAVING SUM(case when lts.status='Активен' then 1 else 0 end)>=1;
--WHERE lts.status='Активен';

--соединяю все вместе тип плнеты и плэйс
SELECT pln.name, plc.name, lts.status, tp.type
FROM planets pln JOIN type_planets tp ON pln.type_planet_id= tp.id
JOIN places plc ON pln.id=plc.planet_id
JOIN lights lts ON plc.id=lts.place_id
WHERE lts.status='Активен' and tp.type='Ледяная' and pln.size > 1000;

--рекурсия просто без всего
WITH RECURSIVE rec_enemy(start, id, enemy) AS (
    SELECT id start, id, enemy
    FROM fanatics
    WHERE enemy IS NOT NULL

    UNION ALL

    SELECT enm.start,fn.id, fn.enemy
    FROM rec_enemy enm
     JOIN fanatics fn ON enm.enemy=fn.id
    WHERE fn.enemy is not null
)
SELECT enm.start,enm.id, enm.enemy, plan.name
FROM rec_enemy enm
JOIN residents res ON enm.start=res.id_fanatic
 JOIN planets pln ON res.id_planet=pln.id
JOIN type_planets tp ON pln.type_planet_id= tp.id
JOIN places plc ON pln.id=plc.planet_id
JOIN lights lts ON plc.id=lts.place_id
JOIN residents ress ON enm.id = ress.id_fanatic
JOIN planets plan ON ress.id_planet=plan.id
WHERE pln.size > 1000
GROUP BY enm.start,enm.id , enm.enemy, plan.name;

