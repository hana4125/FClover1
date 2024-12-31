package hello.fclover.service;

import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import hello.fclover.mybatis.mapper.PaymentMapper;
import hello.fclover.mybatis.mapper.SellerMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper dao;



    @Override
    public Payment save(PaymentReq paymentReq) {
       Payment payment = dao.save(paymentReq);

        return payment;
    }

    @Override
    public int savePayment(Payment payment) {
        int result = dao.savePayment(payment);

            return result;

    }


}
