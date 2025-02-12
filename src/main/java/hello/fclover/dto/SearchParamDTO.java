package hello.fclover.dto;

import lombok.Data;

@Data
public class SearchParamDTO {

    // 검색 키워드
    private String keyword;

    private String processedKeyword;
    private String language;

    // 검색 필터 관련 파라미터
    private int cateNo;

    private String cname;
    private String chrc;
    private String pbcm;

    private Integer saprMin;
    private Integer saprMax;

    private String rlseDate;
    private String reKeyword;

    // 페이징 관련 파라미터
    private String sort = "latest";
    private int page = 1;
    private int size = 20;
    private int offset;
}
