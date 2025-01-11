package hello.fclover.service;


import hello.fclover.domain.Delivery;
import hello.fclover.domain.Payment;
import hello.fclover.domain.Seller;
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

    @Override
    public List<Seller> searchSeller() {
        List<Seller> sellers = dao.searchSeller();
        return sellers;
    }

    @Override
    public void InsertdeliveryReadyList(Long orderId, String userId) {

         String status = "ready";

          dao.insertDeliveryReadyList(orderId,userId,status);
    }

    @Override
    public List<Delivery> deliveryReadyOrderSearch() {
            List<Delivery> deliveries = dao.deliveryReadyOrderSearch();
        return deliveries;
    }

}
