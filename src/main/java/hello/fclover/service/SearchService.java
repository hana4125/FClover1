package hello.fclover.service;

import hello.fclover.dto.GoodsSearchParam;
import java.util.Map;

public interface SearchService {

    int countByKeyword(String keyword);

    Map<String, Object> searchByKeyword(String keyword, String sort, int offset, int size);

    Map<String, Object> searchDetail(GoodsSearchParam goodsSearchParam, String sort, int offset, int size);
}
