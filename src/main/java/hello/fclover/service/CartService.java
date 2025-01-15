package hello.fclover.service;

public interface CartService {
    boolean checkExistInCart(Long goodsNo, Long memberNo);

    void addCart(Long goodsNo, Long memberNo);

    void updateCart(Long goodsNo, Long memberNo);
}
