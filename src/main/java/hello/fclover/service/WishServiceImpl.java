package hello.fclover.service;

import hello.fclover.mybatis.mapper.WishMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishMapper wishMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean checkWished(Long goodsNo, Long memberNo) {
        return wishMapper.isWishExists(goodsNo, memberNo);
    }

    @Override
    public void addWishlist(Long goodsNo, Long memberNo) {
        wishMapper.insertWish(goodsNo, memberNo);
        redisTemplate.opsForSet().add("member:" + memberNo + ":wish", goodsNo.toString());
    }

    @Override
    public void removeWishlist(Long goodsNo, Long memberNo) {
        wishMapper.deleteWish(goodsNo, memberNo);
        redisTemplate.opsForSet().remove("member:" + memberNo + ":wish", goodsNo.toString());
    }

    @Override
    public List<Long> getWishlistGoodsNos(Long goodsNo, Long memberNo) {
//        return wishMapper.findWishlistGoodsNosByMemberNo(memberNo);
        String key = "member:" + memberNo + ":wish";
        try {
            Set<String> wishSet = redisTemplate.opsForSet().members(key);
            if (wishSet != null && !wishSet.isEmpty()) {
                return wishSet.stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Redis 조회 중 예외 발생 시 로깅 후 RDB에서 데이터 조회
            System.err.println("Redis 조회 오류: " + e.getMessage());
        }
        // Redis에 데이터가 없거나 오류 발생 시 RDB에서 조회
        return wishMapper.findWishlistGoodsNosByMemberNo(goodsNo, memberNo);
    }
}
