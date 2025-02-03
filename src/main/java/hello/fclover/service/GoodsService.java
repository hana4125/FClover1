package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.domain.GoodsImage;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GoodsService {
   void goodsSingleInsert(Goods goods, List<MultipartFile> images, String SellerNumber) throws IOException;

    int getTotalGoodsCount(int cateNo);

    int getTotalBestGoodsCount(Long memberNo);

    Goods findGoodsByNo(Long goodsNo);

    // 찜 상태를 포함한 상품 목록 조회 메서드 추가
    List<Goods> getGoodsWithWishStatusList(Long memberNo, int cateNo, String sort, int page, int size);

    List<Goods> getGoodsList(int limit);

    List<Goods> getGoodsWishStatus(Long memberNo, int page, int size);

    void getGoodsDetail(Long goodsNo, Model model);

    GoodsImage getMainImageByGoodsNo(Long goodsNo);

    List<Goods> getNewItems(Long memberNo, String year, String month, String week, int page, int size);

    int getTotalNewItemsCount(Long memberNo, String year, String month, String week);
}

