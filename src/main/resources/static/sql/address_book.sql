CREATE TABLE address_book
(
#   address_no 로 pk 수정
    address_no    INT AUTO_INCREMENT PRIMARY KEY,
    mem_no        INT          NOT NULL,
    address_name   VARCHAR(50)  NOT NULL,
    recipient_name VARCHAR(100) NOT NULL,
    phone_number   VARCHAR(20)  NOT NULL,
    address        VARCHAR(100) NOT NULL,
    is_default     TINYINT(1) DEFAULT 0,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP
);