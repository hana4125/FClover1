package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Payment;
import hello.fclover.domain.Seller;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BackOfficeMapper {

    List<Payment> searchOrder();

    List<Seller> searchSeller();

//    int getProduct_stock(Integer goodsNo);

//    int decrease(int stockCount);
}
