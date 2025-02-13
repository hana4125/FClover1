package hello.fclover.mybatis.mapper;

import hello.fclover.domain.*;
import hello.fclover.dto.PaymentDeliveryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BackOfficeMapper {

    List<Payment> searchOrder();

    List<Seller> searchSeller();

    void insertDeliveryReadyList(Long paymentsNo, String userId, String status);

    List<PaymentDeliveryDTO> deliveryReadyOrderSearch();

    void insertTrackingNumber(Long deliNo, Payment payment, String deliStatus);

    List<Delivery> deliveryInTransitOrderSearch();

    List<Delivery> deliveryDoneSearch();

    void changeDeliveryDoneStatus(int deliNo, String deliStatus);

    List<Settlement> sellerSettlementSearch();

    List<Goods> sellerGoodsApprovalSearch();

    void goodsConfirmSuccess(Long goodsNo);

    List<Seller> sellerPendingCheck();

    void updateSellerApproved(Long sellerNo);

    void updateSellerRejected(Long sellerNo);
}
