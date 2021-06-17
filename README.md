## 菜鸟社区

##资料  <br>
[Spring 文档](https://spring.io/guides) <br>
[Spring Web](https://spring.io/guides/gs/serving-web-conteng/) <br>
[elasticsearch](https://elasticsearch.cn/explore) <br>
[Bootstrap](https://v3.bootcss.com/getting-started) <br>
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)

##工具  <br>
[Git](https://git-scm.com/download) <br>
[Visual Paradigm](https://www.visual-paradigm.com) <br>
[Flyway](https://flywaydb.org/getstarted/firststeps/maven) <br>
[Lombok](https://projectlombok.org/)

##脚本
```sql
create table USER
(
	ID INT auto_increment,
	ACCOUNT_ID VARCHAR(100),
	NAME VARCHAR(50),
	TOKEN CHAR(36),
	GMT_CREATE BIGINT,
	GMT_MODIFIED BIGINT,
	constraint USER_PK
		primary key (ID)
);


```