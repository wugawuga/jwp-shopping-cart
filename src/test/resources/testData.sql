DROP TABLE IF EXISTS CART;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS MEMBER;

CREATE TABLE PRODUCT (
    id          INT           NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(50)   NOT NULL,
    image       VARCHAR(255)   NOT NULL,
    price       INT           NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE MEMBER (
    id          INT           NOT NULL AUTO_INCREMENT,
    email       VARCHAR(255)  NOT NULL UNIQUE,
    password    VARCHAR(255)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE CART (
    id          INT           NOT NULL AUTO_INCREMENT,
    product_id  INT           NOT NULL,
    member_id   INT           NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES PRODUCT (id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES MEMBER (id) ON DELETE CASCADE
);

insert into PRODUCT(`name`, image, price) values ('item1', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS5c6VkPCiNvUmomb-iGTLqP76uu9FOsJWRpg&usqp=CAU', 1000);
insert into PRODUCT(`name`, image, price) values ('item2', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS5c6VkPCiNvUmomb-iGTLqP76uu9FOsJWRpg&usqp=CAU', 2000);
insert into PRODUCT(`name`, image, price) values ('item3', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS5c6VkPCiNvUmomb-iGTLqP76uu9FOsJWRpg&usqp=CAU', 3000);

insert into MEMBER(email, password) values('wuga@naver.com', '1234');
insert into MEMBER(email, password) values('wuga@google.com', '1234');
insert into MEMBER(email, password) values('wuga@nanan.com', '1234');
insert into MEMBER(email, password) values('wuga@nakakver.com', '1234');
