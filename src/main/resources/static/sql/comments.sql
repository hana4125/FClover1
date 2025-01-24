CREATE TABLE comments (
c_no BIGINT AUTO_INCREMENT PRIMARY KEY,
member_id VARCHAR(20),
c_content VARCHAR(300),
c_response_at DATE,
q_no BIGINT,
FOREIGN KEY (q_no) REFERENCES QnA(q_no)
ON DELETE CASCADE
);




drop table comments;
select * from comments;