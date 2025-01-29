CREATE TABLE WISH
(
    WISH_NO     bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_no   bigint, #구매자 아이디
    GOODS_NO    BIGINT NOT NULL #상품 번호
);

commit;

drop table wish;

select * from wish;

SELECT
    b.goods_no,
    b.goods_name,
    c.cate_no,
    CASE
        WHEN w.member_no IS NOT NULL THEN 'YES'
        ELSE 'NO'
        END AS isWished
FROM
    goods b
        JOIN
    category c ON b.cate_no = c.cate_no
        LEFT JOIN
    wish w ON b.goods_no = w.goods_no AND w.member_no = :logged_in_user_id
WHERE
    b.cate_no = :cate_no
ORDER BY
    b.goods_no;


