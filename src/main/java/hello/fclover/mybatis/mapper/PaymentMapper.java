package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import hello.fclover.dto.PaymentGoodsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PaymentMapper {
    Payment save(PaymentReq paymentReq);

    int savePayment(Payment payment);

    Payment findByImpUid(String uid);

    List<PaymentGoodsDTO> searchList(String userId);

    List<Payment> findByPaymentDateBetweenAndStatus(String startDate, String endDate, String paymentCompleted);

    PaymentGoodsDTO searchOneOrderDetail(String userId, Long orderId);
}
