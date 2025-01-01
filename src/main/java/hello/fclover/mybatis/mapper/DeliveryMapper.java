package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Delivery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeliveryMapper {

    int addDelivery(Delivery delivery);
}
