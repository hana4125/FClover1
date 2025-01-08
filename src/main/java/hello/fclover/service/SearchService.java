package hello.fclover.service;

import hello.fclover.domain.Goods;
import java.util.List;

public interface SearchService {

    List<Goods> simpleSearch(String keyword);

}
