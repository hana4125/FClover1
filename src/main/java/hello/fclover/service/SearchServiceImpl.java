package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.mybatis.mapper.GoodsMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private GoodsMapper goodsMapper;

    @Override
    public List<Goods> simpleSearch(String keyword) {
        List<Goods> list = new ArrayList<>();

        // TODO 로직 구현해야 함

        return list;
    }
}
