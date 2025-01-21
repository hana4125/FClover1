package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchDetailForm;
import hello.fclover.mybatis.mapper.GoodsMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final GoodsMapper goodsMapper;

    @Override
    public int countByKeyword(String keyword) {
        return goodsMapper.countGoodsByKeyword(keyword);
    }

    @Override
    public List<Goods> searchByKeyword(String keyword, String sort, int offset, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("sort", sort);
        params.put("offset", offset);
        params.put("size", size);

        return goodsMapper.findGoodsByKeyword(params);
    }

    @Override
    public List<Goods> searchDetail(SearchDetailForm searchDetailForm) {
        return goodsMapper.findGoodsByDetail(searchDetailForm);
    }


}
