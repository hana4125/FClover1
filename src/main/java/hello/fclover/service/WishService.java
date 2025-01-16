package hello.fclover.service;

public interface WishService {
    boolean checkWished(Long goodsNo, Long memberNo);

    void addWishlist(Long goodsNo, Long memberNo);

    void removeWishlist(Long goodsNo, Long memberNo);
}

