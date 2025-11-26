select count(*) from article;

explain select * from article where board_id = 1 order by article_id desc limit 30 offset 90;

explain select * from article where board_id = 1 order by article_id desc limit 30 offset 1499970;

select * from article limit 1;
explain select * from article where article_id = 251594244406456320;

explain select board_id, article_id from article where board_id = 1 order by article_id desc limit 30 offset 1499970;

explain select * from (select article_id from article where board_id = 1 order by article_id desc limit 30 offset 1499970) t left join article on t.article_id = article.article_id;

explain select * from (select article_id from article where board_id = 1 order by article_id desc limit 30 offset 8999970) t left join article on t.article_id = article.article_id;

explain select count(*) from (select article_id from article where board_id = 1 limit 300301) t;

select * from article where board_id = 1 order by article_id desc limit 30;

select * from article where board_id =1 and article_id <251596649101604283 order by article_id desc limit 30;

select * from article where board_id = 1 order by article_id asc limit 1 offset 30;

select * from article where board_id =1 and article_id <251594244448399371 order by article_id desc limit 30;
