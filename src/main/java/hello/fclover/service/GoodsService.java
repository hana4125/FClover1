package hello.fclover.service;

import hello.fclover.domain.Goods;
import java.util.List;

public interface GoodsService {

    List<Goods> getGoodsList(int cateNo, String sort, int page, int size);

    int getTotalGoodsCount(int cate_no);

    Goods findGoodsByNo(Long goodsNo);
}


