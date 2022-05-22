
create table SEC_USER
(
  userId           BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userName         VARCHAR(36) NOT NULL UNIQUE,
  encryptedPassword VARCHAR(128) NOT NULL,
  ENABLED           BIT NOT NULL 
) ;


create table SEC_ROLE
(
  roleId   BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  roleName VARCHAR(30) NOT NULL UNIQUE
  
) ;

create table PRODUCT
(
  productId   BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  productName VARCHAR(100),
  price 	  DECIMAL(18,2),
  category    VARCHAR(100)
) ;

create table ORDERHISTORY
(
  orderId   BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  orderCode VARCHAR(100),
  orderDate DATE,
  userEmail VARCHAR(100)
) ;


create table USER_ROLE
(
  ID      BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userId BIGINT NOT NULL,
  roleId BIGINT NOT NULL
);

alter table USER_ROLE
  add constraint USER_ROLE_UK unique (userId, roleId);

alter table USER_ROLE
  add constraint USER_ROLE_FK1 foreign key (userId)
  references SEC_USER (userId);
 
alter table USER_ROLE
  add constraint USER_ROLE_FK2 foreign key (roleId)
  references SEC_ROLE (roleId);

insert into SEC_User (userName, encryptedPassword, ENABLED)
values ('TomBrady', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);
 
insert into SEC_User (userName, encryptedPassword, ENABLED)
values ('member', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);
 
insert into SEC_User (userName, encryptedPassword, ENABLED)
values ('student', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);
 
insert into sec_role (roleName)
values ('ADMIN');
 
insert into sec_role (roleName)
values ('MEMBER');
 
insert into sec_role (roleName)
values ('GUESTS');
 
insert into user_role (userId, roleId)
values (1, 1);
 
insert into user_role (userId, roleId)
values (1, 2);
 
insert into user_role (userId, roleId)
values (2, 2);


 
insert into user_role (userId, roleId)
values (3, 2);
 

insert into PRODUCT (productName, price, category) 
values ('Asparagus', 3,'Vegetables');

insert into PRODUCT (productName,price, category) 
values ('Broccoli', 6,'Vegetables');

insert into PRODUCT (productName,price, category) 
values ('Carrots', 2.5,'Vegetables');

insert into PRODUCT (productName,price, category) 
values ('Mushrooms', 3,'Vegetables');


insert into PRODUCT (productName,price, category) 
values ('Apples', 10,'Fruits');

insert into PRODUCT (productName,price, category) 
values ('Avocados', 15.10,'Fruits');

insert into PRODUCT (productName,price, category) 
values ('Cherries', 3.25,'Fruits');

insert into PRODUCT (productName,price, category) 
values ('Oranges', 8,'Fruits');

COMMIT;

