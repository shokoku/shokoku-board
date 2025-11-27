use test_db;

select * from lock_test;

start transaction;

update lock_test set content='test2' where id=1234;

start transaction;

update lock_test set content='test2' where id=1234;

select * from performance_schema.data_locks;

commit;

select * from performance_schema.data_locks;
