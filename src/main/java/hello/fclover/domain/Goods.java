package hello.fclover.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Goods {
    // 임시로 ERD 대로 생성
    private int goods_no;               // 상품 번호
    private String seller_id;           // 판매자 아이디
    private int cate_no;                // 카테고리 번호
    private String goods_name;          // 상품 명
    private String goods_content;       // 상품 설명
    private int goods_price;            // 상품 가격
    private String goods_writer;        // 상품 저자
    private String seller_company;      // 출판사
    private LocalDate goods_create_at;  // 상품 발행일
    private String goods_image;         // 상품 이미지 링크
    private int goods_count;            // 상품 총 판매수량
    private LocalDateTime goods_date;   // 상품 등록일

}
