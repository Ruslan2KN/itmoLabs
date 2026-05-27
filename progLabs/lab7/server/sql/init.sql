CREATE TABLE IF NOT EXISTS users (
                                     id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     username TEXT NOT NULL UNIQUE,
                                     password_hash TEXT NOT NULL
);

CREATE TYPE form_of_education AS ENUM (
    'DISTANCE_EDUCATION',
    'FULL_TIME_EDUCATION',
    'EVENING_CLASSES'
);

CREATE TYPE color AS ENUM (
    'RED',
    'BLACK',
    'ORANGE',
    'WHITE'
);

CREATE TYPE country AS ENUM (
    'GERMANY',
    'FRANCE',
    'VATICAN'
);

CREATE TABLE IF NOT EXISTS locations (
                                         id INT GENERATED ALWAYS AS IDENTITY CONSTRAINT pk_id_locations PRIMARY KEY,
                                         x DOUBLE PRECISION NOT NULL,
                                         y BIGINT NOT NULL,
                                         name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS admins (
                                      id INT GENERATED ALWAYS AS IDENTITY CONSTRAINT pk_id_admins PRIMARY KEY,
                                      name TEXT NOT NULL,
                                      height DOUBLE PRECISION CHECK (height > 0),
    eye_color color NOT NULL,
    nationality country NOT NULL,
    id_location INT CONSTRAINT fk_id_locations_admins REFERENCES locations(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS study_groups (
                                            id BIGINT GENERATED ALWAYS AS IDENTITY CONSTRAINT pk_study_groups_id PRIMARY KEY,
                                            name TEXT NOT NULL,
                                            coord_x REAL NOT NULL,
                                            coord_y DOUBLE PRECISION NOT NULL CHECK (coord_y <= 689),
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    students_count INT NOT NULL CHECK (students_count > 0),
    transferred_students INT CHECK (transferred_students > 0),
    average_mark DOUBLE PRECISION CHECK (average_mark > 0),
    form_of_education form_of_education,
    id_admin INT CONSTRAINT fk_id_admin_study_groups REFERENCES admins(id) ON DELETE SET NULL,
    owner_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE
    );