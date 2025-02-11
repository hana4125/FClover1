package hello.fclover.dto;

import hello.fclover.domain.Category;
import hello.fclover.domain.Goods;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class SearchResponseDTO {

    // 검색 결과 관련 정보
    private String keyword;
    private int totalCount;
    private List<Goods> searchResults;
    private Integer minPrice;
    private Integer maxPrice;

    // 필터 관련 정보
    private Map<Category, Integer> categoryList;

    // 페이징 관련 정보
    private String sort;
    private int currentPage = 1;
    private int totalPages;
    private int size = 20;
    private int startPage = 1;
    private int endPage = 10;

}
