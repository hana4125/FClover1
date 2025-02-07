package hello.fclover.dto;

import lombok.Data;

@Data
public class SearchDetailParamDTO {

    private String cname;           // 상품명
    private String chrcDetail;      // 저자
    private String pbcmDetail;      // 출판사
    private String repKeyword;      // 대표 키워드 (값)
    private String repKeywordTarget;// 대표 키워드 종류 (상품명, 저자/역자, 출판사)

    private Integer cate;           // 카테고리

    private Integer saprmin;        // 최소 금액
    private Integer saprmax;        // 최대 금액

    private String rlseStr;         // 시작 발행 날짜 (yyyyMMdd 형태)
    private String rlseEnd;         // 끝 발행 날짜 (yyyyMMdd 형태)

}
