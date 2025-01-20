package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchDetailForm;
import java.util.List;

public interface SearchService {

    int countByKeyword(String keyword);

    List<Goods> searchByKeyword(String keyword, String sort, int offset, int size);

    List<Goods> searchDetail(SearchDetailForm searchDetailForm);
}
