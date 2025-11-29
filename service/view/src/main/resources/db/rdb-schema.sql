create database article_view;

use article_view;

create table article_view_count(
    article_id bigint not null primary key,
    view_count bigint not null
)
