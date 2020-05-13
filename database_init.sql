create database agileboard;

\connect agileboard

create schema agile;

begin;

create table agile.projects
(
    id          serial primary key,
    name        varchar(255),
    description text
);

create table agile.sprints
(
    id         serial primary key,
    name       varchar(255),
    no         integer                                                  not null,
    project_id integer references agile.projects (id) on delete cascade not null
);

create table agile.progresses
(
    id          integer primary key,
    name        varchar(255) not null,
    description varchar(255) not null
);

insert into agile.progresses
values (1, 'TO DO', 'to do elements'),
       (2, 'IN PROGRESS', 'tasks in progress'),
       (3, 'REVIEW', 'task reported to review'),
       (4, 'APPROVED REVIEW', 'task after positive review'),
       (5, 'REVIEW REJECTED', 'rejected review'),
       (6, 'DONE', 'done task');

create table agile.tasks
(
    id          serial primary key,
    description varchar(255),
    estimation  integer,
    progress_id integer references agile.progresses (id) on delete cascade not null,
    sprint_id   integer references agile.sprints (id) on delete cascade    not null
);

commit;