package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PaymentMapper {
    Payment save(PaymentReq paymentReq);

    int savePayment(Payment payment);

    Payment findByImpUid(String uid);

    List<Payment> searchList(String userId);

    List<Payment> findByPaymentDateBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, String paymentCompleted);
}
