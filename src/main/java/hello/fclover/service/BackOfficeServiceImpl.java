package hello.fclover.service;


import hello.fclover.domain.*;
import hello.fclover.mybatis.mapper.BackOfficeMapper;
import hello.fclover.mybatis.mapper.SellerMapper;
import hello.fclover.mybatis.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public void InsertdeliveryReadyList(Long paymentsNo, String userId) {

        log.info("==============> 여기는 백오피스컨트롤러 paymentsNo: " + paymentsNo+ " userId: " + userId);
         String status = "배송준비중";

          dao.insertDeliveryReadyList(paymentsNo,userId,status);
    }

    @Override
    public List<Delivery> deliveryReadyOrderSearch() {
            List<Delivery> deliveries = dao.deliveryReadyOrderSearch();
        return deliveries;
    }

    @Override
    public void insertTrackingNumber(int deliNo, int deliveryNum, String deliveryCompany,String deliStatus) {

//        System.out.println("deliNo = " + deliNo);
//        System.out.println("deliveryNum = " + deliveryNum);
//        System.out.println("deliveryCompany = " + deliveryCompany);

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

    @Override
    public List<Settlement> sellerSettlementSearch() {
        List<Settlement> list = dao.sellerSettlementSearch();
        return list;
    }

    @Override
    public List<Goods> sellerGoodsApprovalSearch() {
        List<Goods> list = dao.sellerGoodsApprovalSearch();


        return list;
    }

    @Override
    public void goodsConfirmSuccess(Long goodsNo) {
        dao.goodsConfirmSuccess(goodsNo);
    }

    @Override
    public List<Seller> sellerPendingCheck() {
        return dao.sellerPendingCheck();
    }

    @Override
    public void updateSellerApproved(Long sellerNo) {
        dao.updateSellerApproved(sellerNo);
    }

    @Override
    public void updateSellerRejected(Long sellerNo) {
        dao.updateSellerRejected(sellerNo);
    }
}
