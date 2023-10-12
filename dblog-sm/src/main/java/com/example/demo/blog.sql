
drop database if exists mycnblog2023;
create database mycnblog2023 DEFAULT CHARACTER SET utf8mb4;;
use mycnblog2023;



drop table if exists ArticleInfo;
CREATE TABLE ArticleInfo (
     id INT AUTO_INCREMENT PRIMARY KEY,
     title VARCHAR(255) NOT NULL,
     content TEXT,
     createTime DATETIME,
     updateTime DATETIME,
     uid INT,
     rCount INT,
     state INT
) default charset 'utf8mb4';
insert into ArticleInfo values (null,'java','java正文',now(),now(),2,1,1);
-- 创建 Userinfo 表
drop table if exists Userinfo;
CREATE TABLE Userinfo (
      id INT AUTO_INCREMENT PRIMARY KEY,
      username VARCHAR(255) NOT NULL unique ,
      password VARCHAR(255) NOT NULL,
      photo VARCHAR(255),
      createTime DATETIME,
      updateTime DATETIME,
      state INT
) default charset 'utf8mb4';
