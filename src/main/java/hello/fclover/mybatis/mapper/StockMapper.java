package hello.fclover.mybatis.mapper;

import hello.fclover.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockMapper {

    int decrease(int stockCount);
}
