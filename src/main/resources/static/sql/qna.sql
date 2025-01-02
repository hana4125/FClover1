CREATE TABLE QnA(
    q_num int,
    q_create_at date,
    mem_id varchar(20),
    q_response_at date,
    q_name varchar(30),
    q_type varchar(20),
    q_file varchar(255),
    q_title varchar(20),
    q_content varchar(300),
    response_phone varchar(20),
    response_email varchar(100),
    q_alert varchar(1),
    q_re_ref int,
    q_re_lev int,
    q_re_seq int,
    PRIMARY KEY (q_num)
);

DROP TABLE QnA;

select * from QnA;