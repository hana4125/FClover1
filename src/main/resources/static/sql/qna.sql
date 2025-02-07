CREATE TABLE qna(
    q_no bigint,
    q_create_at date,
    member_id varchar(20),
    q_name varchar(30),
    q_type varchar(20),
    q_file varchar(255),
    q_title varchar(30),
    q_content varchar(500),
    response_phone varchar(20),
    response_email varchar(100),
    q_alert varchar(1),
    q_re_ref int,
    q_re_lev int,
    q_re_seq int,
    PRIMARY KEY (q_no)
);

select * from qna;

ALTER TABLE qna MODIFY COLUMN q_alert ENUM('y', 'n');


SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE qna;
SET FOREIGN_KEY_CHECKS = 1;


