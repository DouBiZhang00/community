create table QUESTION
(
    ID BIGINT auto_increment
        primary key,
    TITLE VARCHAR(50),
    DESCRIPTION TEXT,
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT,
    CREATOR BIGINT,
    COMMENT_COUNT INT default 0,
    VIEW_COUNT INT default 0,
    LIKE_COUNT INT default 0,
    TAG VARCHAR(256)
);

