create table book (
    id bigserial primary key not null,
    author varchar(255) not null,
    isbn varchar(255) unique not null,
    price float not null,
    title varchar(255) not null,
    created_date timestamp,
    last_modified_date  timestamp,
    version integer NOT NULL
);