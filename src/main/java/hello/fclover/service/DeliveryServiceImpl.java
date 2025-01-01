package hello.fclover.service;

import hello.fclover.domain.Delivery;
import hello.fclover.mybatis.mapper.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryMapper deliveryMapper;

    @Override
    public int addDelivery(Delivery delivery) {
        return deliveryMapper.addDelivery(delivery);
    }
}
