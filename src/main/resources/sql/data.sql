create table if not exists Logs(
    id varchar(50) not null primary key,
    duration bigint not null,
    host varchar(50),
    type varchar(50),
    alert boolean
);
