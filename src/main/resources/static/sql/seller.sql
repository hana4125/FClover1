DROP TABLE seller;

CREATE TABLE seller
(
    seller_no       BIGINT          AUTO_INCREMENT PRIMARY KEY,
    seller_id       varchar(100) NOT NULL UNIQUE,
    password        varchar(100) DEFAULT NULL,
    name            varchar(20)  NOT NULL,
    email           varchar(100) NOT NULL,
    phone_number    varchar(20)  DEFAULT NULL,
    business_number varchar(200) DEFAULT NULL,
    company_name    varchar(100) DEFAULT NULL,
    admission_at    datetime     DEFAULT NULL,
    created_at      datetime     DEFAULT CURRENT_TIMESTAMP,
    updated_at      datetime     DEFAULT NULL,
    FULLTEXT KEY ft_seller_index (company_name) WITH PARSER ngram
) ENGINE=InnoDB;
SELECT * FROM seller;

-- DTO, TABLE 변경. 수정 필요
INSERT INTO seller
(seller_id, seller_pass , seller_name, seller_email, seller_phone, seller_number,
 company_name, admisson_at, seller_birth, seller_gender, updated_at )
VALUES ('seller001', 'password123', '홍길동', 'hong@example.com', '010-1234-5678', '123-45-67890', '홍길동전자', '2023-01-15',
        '19900101', 'M', '2023-01-15 10:30:00') ,
('seller002', 'password234', '김철수', 'kim@example.com', '010-2345-6789', '234-56-78901', '김철수주식회사', '2023-02-20', '19851212', 'M', '2023-02-20 11:00:00')
,
('seller003', 'password345', '이영희', 'lee@example.com', '010-3456-7890', '345-67-89012', '이영희디지털', '2023-03-25', '19920110', 'F', '2023-03-25 09:45:00')
,
('seller004', 'password456', '박상우', 'park@example.com', '010-4567-8901', '456-78-90123', '박상우테크', '2023-04-10', '19871205', 'M', '2023-04-10 13:20:00')
,
('seller005', 'password567', '정유진', 'jung@example.com', '010-5678-9012', '567-89-01234', '정유진컴퍼니', '2023-05-30', '19931215', 'F', '2023-05-30 14:10:00')
,
('seller006', 'password678', '최민수', 'choi@example.com', '010-6789-0123', '678-90-12345', '최민수산업', '2023-06-05', '19951030', 'M', '2023-06-05 16:25:00')
,
('seller007', 'password789', '오은영', 'oh@example.com', '010-7890-1234', '789-01-23456', '오은영글로벌', '2023-07-22', '19910818', 'F', '2023-07-22 17:00:00')
,
('seller008', 'password890', '장영호', 'jang@example.com', '010-8901-2345', '890-12-34567', '장영호시스템', '2023-08-11', '19930809', 'M', '2023-08-11 12:40:00')
,
('seller009', 'password901', '임지현', 'lim@example.com', '010-9012-3456', '901-23-45678', '임지현기술', '2023-09-03', '19940122', 'F', '2023-09-03 10:10:00')
,
('seller010', 'password012', '송하늘', 'song@example.com', '010-0123-4567', '012-34-56789', '송하늘코퍼레이션', '2023-10-12', '19921020', 'F', '2023-10-12 15:35:00');