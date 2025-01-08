package hello.fclover.service;

import hello.fclover.domain.Goods;
import java.util.List;

public interface GoodsService {

    List<Goods> getGoodsList(int cate_no);

    Goods getGoodsById(int goods_no);
}


