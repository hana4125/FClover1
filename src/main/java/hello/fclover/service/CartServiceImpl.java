package hello.fclover.service;

import hello.fclover.mybatis.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    @Override
    public boolean checkExistInCart(Long goodsNo, Long memberNo) {
        return cartMapper.isCartExists(goodsNo, memberNo);
    }

    @Override
    public void addCart(Long goodsNo, Long memberNo) {
        cartMapper.insertCart(goodsNo, memberNo);
    }

    @Override
    public void updateCart(Long goodsNo, Long memberNo) {
        cartMapper.updateCartQuantity(goodsNo, memberNo);
    }
}
