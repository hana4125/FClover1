package hello.fclover.service;

import hello.fclover.domain.Goods;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GoodsService {
   void goodsSingleInsert(Goods goods, List<MultipartFile> images, String SellerNumber) throws IOException;

    int getTotalGoodsCount(int cateNo);

    int getTotalBestGoodsCount();

    Goods findGoodsByNo(Long goodsNo);

    // 찜 상태를 포함한 상품 목록 조회 메서드 추가
    List<Goods> getGoodsWithWishStatusList(Long memberNo, int cateNo, String sort, int page, int size);

    List<Goods> getGoodsList();

    List<Goods> getGoodsWishStatus(Long memberNo, int page, int size);
  
    void getGoodsDetail(Long goodsNo, Model model);
}

