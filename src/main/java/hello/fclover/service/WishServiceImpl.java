package hello.fclover.service;

import hello.fclover.domain.Wish;
import hello.fclover.dto.WishDTO;
import hello.fclover.mybatis.mapper.WishMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishMapper wishMapper;

    @Override
    public boolean checkWished(Long goodsNo, Long memberNo) {
        return wishMapper.isWishExists(goodsNo, memberNo);
    }

    @Override
    public void addWishlist(Long goodsNo, Long memberNo) {
        wishMapper.insertWish(goodsNo, memberNo);
    }

    @Override
    public void removeWishlist(Long goodsNo, Long memberNo) {
        wishMapper.deleteWish(goodsNo, memberNo);
    }

    @Override
    public List<Long> getWishlistGoodsNos(Long memberNo) {
        return wishMapper.findWishlistGoodsNosByMemberNo(memberNo);
    }

    @Override
    public List<Wish> getWishList(Long memberNo) {
        return wishMapper.selectWishList(memberNo);
    }

    @Override
    public List<WishDTO> getWishDTOList(Long memberNo) {
        return wishMapper.selectWishDTOList(memberNo);
    }
}
