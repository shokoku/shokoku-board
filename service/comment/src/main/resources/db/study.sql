select * from (
    select comment_id from comment
    where article_id = 1
    order by parent_comment_id asc, comment_id asc
    limit 1 offset 1
) t left join comment on t.comment_id = comment.comment_id;

select count(*) from (
    select comment_id from comment where article_id = 1 limit 1
) t;

select * from comment
where article_id = 1 and (
    parent_comment_id > 1 or
    (parent_comment_id = 1 and comment_id > 1)
    )
order by parent_comment_id asc, comment_id asc
limit 1

select table_name, table_collation from information_schema.TABLES where table_schema = 'comment';

select table_name, column_name, collation_name from information_schema.COLUMNS where table_schema = 'comment' and TABLE_NAME = 'comment_v2' and COLUMN_NAME = 'path';

explain select path from comment_v2
    where article_id =1
    and path > '00a0z'
    and path like '00a0z%'
    order by path desc limit 1;
