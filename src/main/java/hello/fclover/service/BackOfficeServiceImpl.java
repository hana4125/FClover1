package hello.fclover.service;


import hello.fclover.domain.Delivery;
import hello.fclover.domain.Payment;
import hello.fclover.domain.Seller;
import hello.fclover.mybatis.mapper.BackOfficeMapper;
import hello.fclover.mybatis.mapper.SellerMapper;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
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

        log.info("==============> 여기는 백오피스컨트롤러 orderId: " + orderId + " userId: " + userId);
         String status = "배송준비중";

          dao.insertDeliveryReadyList(orderId,userId,status);
    }

    @Override
    public List<Delivery> deliveryReadyOrderSearch() {
            List<Delivery> deliveries = dao.deliveryReadyOrderSearch();
        return deliveries;
    }

    @Override
    public void insertTrackingNumber(int deliNo, int deliveryNum, String deliveryCompany,String deliStatus) {

        System.out.println("deliNo = " + deliNo);
        System.out.println("deliveryNum = " + deliveryNum);
        System.out.println("deliveryCompany = " + deliveryCompany);

        //배송번호에 대한 운송장 정보 저장
        dao.insertTrackingNumber(deliNo,deliveryNum,deliveryCompany,deliStatus);


        //
    }

    @Override
    public List<Delivery> deliveryInTransitOrderSearch() {
        List<Delivery> deliveries = dao.deliveryInTransitOrderSearch();
        return deliveries;
    }

    @Override
    public List<Delivery> deliveryDoneOrderSearch() {
        List<Delivery> deliveries = dao.deliveryDoneSearch();
        return deliveries;
    }

    @Override
    public void changeDeliveryDoneStatus(int deliNo) {
        String deliStatus = "배송완료";
        dao.changeDeliveryDoneStatus(deliNo,deliStatus);
    }

}
