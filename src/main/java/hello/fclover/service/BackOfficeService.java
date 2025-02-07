package hello.fclover.service;

import hello.fclover.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BackOfficeService {

    List<Payment> searchOrder();

    List<Seller> searchSeller();

    void InsertdeliveryReadyList(Long paymentsNo, String userId);

    List<Delivery> deliveryReadyOrderSearch();

    void insertTrackingNumber(int deliNo, int deliveryNum, String deliveryCompany,String deliStatus);

    List<Delivery> deliveryInTransitOrderSearch();

    List<Delivery> deliveryDoneOrderSearch();

    void changeDeliveryDoneStatus(int deliNo);

    List<Settlement> sellerSettlementSearch();

    List<Goods> sellerGoodsApprovalSearch();

    void goodsConfirmSuccess(Long goodsNo);

    List<Seller> sellerPendingCheck();

    void updateSellerApproved(Long sellerNo);

    void updateSellerRejected(Long sellerNo);
}
