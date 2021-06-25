create table comment
(
    id BIGINT AUTO_INCREMENT primary key ,
    parent_id BIGINT,
    type int,
    gmt_create BIGINT,
    gmt_modified BIGINT,
    like_count BIGINT default 0,
);
