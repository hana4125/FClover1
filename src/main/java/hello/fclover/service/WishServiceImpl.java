package hello.fclover.service;

import hello.fclover.mybatis.mapper.WishMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
}
