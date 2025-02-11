package hello.fclover.service;

import hello.fclover.domain.Category;
import hello.fclover.dto.SearchKeywordDTO;
import hello.fclover.dto.SearchParamDTO;
import java.util.Map;

public interface CountService {

    int countByKeyword(SearchKeywordDTO processedKeyword);

    Map<Category, Integer> getCategoryCount(SearchKeywordDTO processedKeyword);

    Map<Category, Integer> getCategoryCount(SearchParamDTO searchParamDTO);
}
