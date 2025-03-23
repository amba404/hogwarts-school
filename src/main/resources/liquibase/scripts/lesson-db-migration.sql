-- liquibase formatted sql

-- changeset Amba404:1
CREATE INDEX student_idx_name ON student USING GIST (name);

-- changeset Amba404:2
CREATE INDEX faculty_idx_name_color ON faculty USING GIST (name, color);
