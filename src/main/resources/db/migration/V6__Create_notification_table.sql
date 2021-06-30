create table notification(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    notifier BIGINT NOT NULL,
    receiver BIGINT NOT NULL,
    outerId BIGINT NOT NULL,
    type INT NOT NULL,
    gmt_create BIGINT NOT NULL,
    status INT DEFAULT 0 NOT NULL,
    NOTIFIER_NAME VARCHAR(100) NULL,
    OUTER_TITLE varchar (256) NULL
);