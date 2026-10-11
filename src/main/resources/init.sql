create table if not exists payments (
    id serial primary key,
    income numeric(15, 2) not null,
    percent numeric(6, 2) not null,
    tax numeric(15, 2) not null,
    created_at timestamp default now()
);
