package hello.fclover.service;


import java.util.List;

public interface WishService {
    boolean checkWished(Long goodsNo, Long memberNo);

    void addWishlist(Long goodsNo, Long memberNo);

    void removeWishlist(Long goodsNo, Long memberNo);

    List<Long> getWishlistGoodsNos(Long goodsNo, Long memberNo);
}

