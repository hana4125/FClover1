package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    Payment save(PaymentReq paymentReq);
    int savePayment(Payment payment);

}
