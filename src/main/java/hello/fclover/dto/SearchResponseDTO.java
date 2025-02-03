package hello.fclover.dto;

import hello.fclover.domain.Goods;
import java.util.List;
import lombok.Data;

@Data
public class SearchResponseDTO {

    // 검색 결과 관련 정보
    private String keyword;
    private int totalCount;
    private List<Goods> searchResults;

    // 페이징 관련 정보
    private String sort;
    private int currentPage;
    private int totalPages;
    private int size;
    private int startPage;
    private int endPage;

}
