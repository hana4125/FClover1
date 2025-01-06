package hello.fclover.service;

import hello.fclover.domain.Payment;
import hello.fclover.domain.PaymentReq;
import hello.fclover.mybatis.mapper.PaymentMapper;
import hello.fclover.mybatis.mapper.SellerMapper;
import hello.fclover.util.PaymentClient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper dao;
    private final PaymentClient paymentClient;

    @Override
    public List<Payment> searchList(String userId) {
        List<Payment> payments =  dao.searchList(userId);

        return payments;
    }

    @Override
    public Payment save(PaymentReq paymentReq) {
       Payment payment1 = dao.save(paymentReq);

        return payment1;
    }

    @Override
    public int savePayment(Payment payment) {
        int result = dao.savePayment(payment);

            return result;

    }

    @Override
    public void cancelPayment(String uid) {

        // 외부 API로 결제 취소 요청
        String result = paymentClient.cancelPayment(uid);

        if (result.contains("ERROR")) {
            throw new RuntimeException("Payment cancellation failed during recovery process.");
        }

        // impUid로 Payment 테이블 조회
        Payment payment  = dao.findByImpUid(uid);

        // status 필드를 "cancel"로 변경
        payment.setStatus("cancel");
    }

    @Override
    public void decrease(Long id, Long quantity) {
        //상품수량 조회
        //재고 감소 로직 작성
        //갱신된 값을 저장하는 로직
    }


}
