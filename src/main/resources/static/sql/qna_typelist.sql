CREATE TABLE QnA_typelist
(
    q_type  varchar(3) primary key,
    q_value varchar(30)
);

insert into QnA_typelist values ('000','배송/수령예정일안내');
insert into QnA_typelist values ('001','주문/결제/기프트카드');
insert into QnA_typelist values ('002','검색 기능 관련');
insert into QnA_typelist values ('003','반품/교환/환불(도서)');
insert into QnA_typelist values ('004','도서/상품정보');
insert into QnA_typelist values ('005','회원정보서비스');
insert into QnA_typelist values ('006','웹사이트 이용 관련');
insert into QnA_typelist values ('007','시스템불편사항');
insert into QnA_typelist values ('008','서양도서 검색/주문');
insert into QnA_typelist values ('009','일본도서 검색/주문');
insert into QnA_typelist values ('010','매장관련');
insert into QnA_typelist values ('012','택배사사례');
insert into QnA_typelist values ('013','고객제안/친절불친절');
insert into QnA_typelist values ('014','파본/상품불량신고');
insert into QnA_typelist values ('926','북로그/리뷰&amp;리스트');
insert into QnA_typelist values ('935','ebook상품/오류신고');
insert into QnA_typelist values ('939','대량구매');
insert into QnA_typelist values ('941','POD 주문형출판');
insert into QnA_typelist values ('944','개인정보침해신고');
insert into QnA_typelist values ('945','모바일교보문고');
insert into QnA_typelist values ('948','동영상문의');

commit;


