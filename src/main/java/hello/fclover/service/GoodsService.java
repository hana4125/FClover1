package hello.fclover.service;

import hello.fclover.domain.Goods;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GoodsService {
   void goodsSingleInsert(Goods goods, List<MultipartFile> images, String SellerNumber) throws IOException;

//    List<Goods> getGoodsList(int cateNo, String sort, int page, int size);

    int getTotalGoodsCount(int cateNo);

    Goods findGoodsByNo(Long goodsNo);

    // 찜 상태를 포함한 상품 목록 조회 메서드 추가
    List<Goods> getGoodsWithWishStatusList(Long memberNo, int cateNo, String sort, int page, int size);
}

