package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface GoodsService {
   void goodsSingleInsert(Goods goods, List<MultipartFile> images, String SellerNumber) throws IOException;

    List<Goods> getGoodsList(int cateNo, String sort, int page, int size);

    int getTotalGoodsCount(int cate_no);

    Goods findGoodsByNo(Long goodsNo);

    void getGoodsDetail(Long goodsNo, Model model);
}

