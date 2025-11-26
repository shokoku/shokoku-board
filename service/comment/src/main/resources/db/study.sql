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
