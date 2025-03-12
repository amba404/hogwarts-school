create table brands
(
    id   integer primary key,
    name varchar(50) unique not null
);

create table models
(
    id       integer primary key,
    name     varchar(50) not null,
    brand_id integer     not null
        constraint fk_brand_id references brands (id),
    constraint name_brand_id_unique unique (brand_id, name)
);

create table car
(
    id       integer primary key,
    model_id integer not null
        constraint fk_model_id references models (id),
    cost     money
);

create table persons
(
    id          integer primary key,
    name        varchar(50) not null unique,
    age         integer     not null,
    have_rights boolean     not null
);

create table car_users(
    car_id   integer not null
                      constraint fk_car_id references car (id),
    person_id integer not null
                      constraint fk_person_id references persons (id),
    constraint pk_car_users primary key (car_id, person_id)
);