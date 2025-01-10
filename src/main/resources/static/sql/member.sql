CREATE TABLE member
(
    #   mem_no 로 pk 수정
    mem_no         INT AUTO_INCREMENT PRIMARY KEY,
    member_id       VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(100),
    name            VARCHAR(100)  NOT NULL,
    email           VARCHAR(100) NOT NULL,
    birthdate       VARCHAR(20),
    phone_number    VARCHAR(20),
    gender          VARCHAR(10),
    auth            VARCHAR(20)  NOT NULL,
    profile_picture VARCHAR(255) DEFAULT NULL,
    created_at      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME
);


drop table member;
commit;