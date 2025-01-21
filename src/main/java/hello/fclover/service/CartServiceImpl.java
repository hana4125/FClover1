package hello.fclover.service;

import hello.fclover.mybatis.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    @Override
    public int upsertCart(Long goodsNo, Long memberNo) {
        // MyBatis 쿼리 실행
        return cartMapper.upsertCart(goodsNo, memberNo);
    }
}
