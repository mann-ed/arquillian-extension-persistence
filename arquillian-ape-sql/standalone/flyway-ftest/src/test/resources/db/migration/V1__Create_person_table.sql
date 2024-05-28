create table if not exists person (
    id IDENTITY primary key,
    first_name varchar(215),
    last_name varchar(215),
    age  smallint
);
