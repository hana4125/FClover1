package hello.fclover.service;

import hello.fclover.domain.Goods;
import hello.fclover.domain.Settlement;
import hello.fclover.mybatis.mapper.SellerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerMapper dao;

    @Override
    public void goodsSingleInsert(Goods goods) {
        dao.goodsSingleInsert(goods);
    }
    public static Settlement create(Long partnerId, BigDecimal totalAmount, LocalDate paymentDate) {
        Settlement settlement = new Settlement();
        settlement.setPartnerId(partnerId);
        settlement.setTotalAmount(totalAmount);
        settlement.setStatus("completed");
        settlement.setPaymentDate(paymentDate);
        settlement.setCreatedAt(LocalDateTime.now());
        settlement.setUpdatedAt(LocalDateTime.now());
        return settlement;
    }

}
