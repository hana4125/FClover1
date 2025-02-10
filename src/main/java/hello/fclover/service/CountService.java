package hello.fclover.service;

import hello.fclover.domain.Category;
import java.util.Map;

public interface CountService {

    int countByKeyword(String keyword);

    Map<Category, Integer> getCategoryCount(String keyword);
}
