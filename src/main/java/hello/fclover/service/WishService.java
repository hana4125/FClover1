package hello.fclover.service;


import hello.fclover.domain.Wish;
import hello.fclover.dto.WishDTO;

import java.util.List;

public interface WishService {
    boolean checkWished(Long goodsNo, Long memberNo);

    void addWishlist(Long goodsNo, Long memberNo);

    void removeWishlist(Long goodsNo, Long memberNo);

    List<Long> getWishlistGoodsNos(Long memberNo);

    List<Wish> getWishList(Long memberNo);

    List<WishDTO> getWishDTOList(Long memberNo);
}

