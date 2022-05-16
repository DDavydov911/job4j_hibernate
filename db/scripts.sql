create table carmarks
(
    id serial primary key,
    name varchar(255)
);

create table carmodels
(
    id   serial primary key,
    name varchar(255)
);

create table carmarks_carmodels
(
    id serial primary key,
    carmarks_id integer references carmarks(id),
    carmodels_id integer references carmodels(id)
);


