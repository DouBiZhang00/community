create table comment
(
    id BIGINT AUTO_INCREMENT primary key ,
    parent_id BIGINT,
    type int,
    gmt_create BIGINT,
    gmt_modified BIGINT,
    like_count BIGINT default 0,
    commentator BIGINT null,
    content varchar(1024) null,
    comment_count int default 0
);
