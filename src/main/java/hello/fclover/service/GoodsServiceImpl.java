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
    public List<Goods> getGoodsList(int cate_no, String sort, int page, int size) {
        int offset = (page - 1) * size;
        return goodsMapper.findAll(cate_no, sort, offset, size);
    }

    @Override
    public int getTotalGoodsCount(int cate_no) {
        return goodsMapper.countGoods(cate_no);
    }

    @Override
    public Goods getGoodsById(int goods_no) {
        return goodsMapper.findById(goods_no);
    }
}
