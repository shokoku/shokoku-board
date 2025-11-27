create database test_db;

use test_db;

create table lock_test
(
    id      bigint       not null primary key,
    content varchar(100) not null
);

insert into lock_test values (1234, 'test');


start transaction;

update lock_test set content = 'test2' where id = 1234;

select * from performance_schema.data_locks;

commit;

select * from performance_schema.data_locks;
