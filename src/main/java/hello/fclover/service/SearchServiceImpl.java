package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.dto.SearchDetailForm;
import hello.fclover.mybatis.mapper.GoodsMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final GoodsMapper goodsMapper;

    @Override
    public List<Goods> searchByKeyword(String keyword) {
        return goodsMapper.findGoodsByKeyword(keyword);
    }

    @Override
    public List<Goods> searchDetail(SearchDetailForm searchDetailForm) {
        return goodsMapper.findGoodsByDetail(searchDetailForm);
    }


}
