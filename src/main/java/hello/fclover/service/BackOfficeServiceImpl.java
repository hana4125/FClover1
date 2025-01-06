package hello.fclover.service;


import hello.fclover.domain.Payment;
import hello.fclover.mybatis.mapper.BackOfficeMapper;
import hello.fclover.mybatis.mapper.SellerMapper;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BackOfficeServiceImpl implements BackOfficeService {

    private final BackOfficeMapper dao;

    @Transactional(readOnly = true)
    @Override
    public List<Payment> searchOrder() {
        List<Payment> payment = dao.searchOrder();
        return payment;
    }

    @Transactional(readOnly = true)
    @Override
    public int getProduct_stock(int goods_no) {
        int stockCount = dao.getProduct_stock(goods_no);
        return stockCount;
    }

    @Transactional
    @Override
    public int decrease(int stockCount) {
        int resultCount = stockCount-1;
        return resultCount;
    }
}
