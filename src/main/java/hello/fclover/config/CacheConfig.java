package hello.fclover.config;

//import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
//    @Bean
//    public List<CaffeineCache> caffeineCaches() {
//        return Arrays.stream(CacheType.values())
//                .map(cache -> new CaffeineCache(cache.getCacheName(), Caffeine.newBuilder().recordStats()
//                        .expireAfterWrite(cache.getExpiredAfterWrite(), TimeUnit.MINUTES)
//                        .maximumSize(cache.getMaximumSize())
//                        .build()))
//                .toList();
//    }
//    @Bean
//    public CacheManager cacheManager(List<CaffeineCache> caffeineCaches) {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(caffeineCaches);
//
//        return cacheManager;
//    }

    @Bean
    public CacheManager cacheManager() {
        // 필요한 캐시 이름들을 여기에 추가합니다.
        return new ConcurrentMapCacheManager("CategoryMapper.findTitle",
                                            "CategoryMapper.findAll",
                                            "GoodsMapper.findCategoryGoodsWishStatus",
                                            "GoodsMapper.findBestGoodsWishStatus",
                                            "GoodsMapper.findSteadyGoodsWishStatus",
                                            "GoodsMapper.selectNewItems");
    }
}
