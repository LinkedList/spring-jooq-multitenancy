drop schema tenants cascade;
drop schema master cascade;
drop schema shared cascade;
drop schema cust_john cascade;
drop schema cust_jane cascade;

create schema tenants;
create schema master;
create schema shared;
create schema cust_john;
create schema cust_jane;

create table "shared".note_type
(
  id serial not null
    constraint note_type_pkey
    primary key,
  type text not null
);

create table "tenants".tenants
(
  id serial not null
    constraint tenants_pkey
    primary key,
  schema text not null,
  login text not null,
  password text not null
);

create table "master".notes
(
  id serial not null
    constraint notes_pkey
    primary key,
  note text not null,
  note_type integer not null
    constraint notes_note_type_id_fk
    references "shared".note_type (id)
);

create table "cust_john".notes
(
  id serial not null
    constraint notes_pkey
    primary key,
  note text not null,
  note_type integer not null
    constraint notes_note_type_id_fk
    references "shared".note_type (id)
);

create table "cust_jane".notes
(
  id serial not null
    constraint notes_pkey
    primary key,
  note text not null,
  note_type integer not null
    constraint notes_note_type_id_fk
    references "shared".note_type (id)
);
