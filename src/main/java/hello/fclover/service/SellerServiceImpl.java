package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.mybatis.mapper.SellerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerMapper dao;

    @Override
    public void goodsSingleInsert(Goods goods) {
        dao.goodsSingleInsert(goods);
    }
}
