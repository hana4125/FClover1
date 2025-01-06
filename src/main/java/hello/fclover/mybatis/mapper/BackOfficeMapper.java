package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BackOfficeMapper {

    List<Payment> searchOrder();

    int getProduct_stock(Integer goodsNo);

    int decrease(int stockCount);
}
