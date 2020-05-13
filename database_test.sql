create schema if not exists agile;

begin;

create table agile.projects
(
    id          serial primary key,
    name        varchar(255),
    description text
);

insert into agile.projects
values (1, 'project 1', 'project description 1'),
       (2, 'project 2', 'project description 2');

create table agile.sprints
(
    id         serial primary key,
    name       varchar(255),
    no         integer not null,
    project_id integer references agile.projects (id)
);

insert into agile.sprints
values (1, 'sprint 1', 1, 1),
       (2, 'sprint 2', 2, 1);

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
    progress_id integer references agile.progresses (id),
    sprint_id   integer references agile.sprints (id)
);

insert into agile.tasks
values (1, 'task 1 description', 5, 1, 1),
       (2, 'task 2 description', 7, 1, 1);

commit;