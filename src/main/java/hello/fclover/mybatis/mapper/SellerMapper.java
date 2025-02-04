package hello.fclover.mybatis.mapper;


import hello.fclover.domain.Seller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SellerMapper {
    int insertSeller(Seller seller);

    Seller selectSellerById(String sellerId);

    String isSellerIdDuplicate(String sellerId);

    Seller selectSellerByIdPassword(@Param("sellerId") String sellerId, @Param("password") String password);
}
