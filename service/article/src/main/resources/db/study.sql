select count(*) from article;

explain select * from article where board_id = 1 order by article_id desc limit 30 offset 90;

create index idx_board_id_article_id on article(board_id asc, article_id desc);

explain select * from article where board_id = 1 order by article_id desc limit 30 offset 1499970;

select * from article limit 1;
explain select * from article where article_id = 251594244406456320;

explain select board_id, article_id from article where board_id = 1 order by article_id desc limit 30 offset 1499970;

explain select * from (select article_id from article where board_id = 1 order by article_id desc limit 30 offset 1499970) t left join article on t.article_id = article.article_id;

explain select * from (select article_id from article where board_id = 1 order by article_id desc limit 30 offset 8999970) t left join article on t.article_id = article.article_id;
