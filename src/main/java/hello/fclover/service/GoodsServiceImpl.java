package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.mybatis.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;

    @Override
    public List<Goods> getGoodsList(int cate_no) {
        // 상품 리스트 가져오기
        return goodsMapper.findAll(cate_no);
    }

    @Override
    public Goods getGoodsById(int goods_no) {
        return goodsMapper.findById(goods_no);
    }
}
