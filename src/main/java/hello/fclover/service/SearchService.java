package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchDetailForm;
import java.util.List;

public interface SearchService {

    List<Goods> searchByKeyword(String keyword);

    List<Goods> searchDetail(SearchDetailForm searchDetailForm);
}
