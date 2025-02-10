package hello.fclover.mybatis.mapper;


import hello.fclover.domain.Seller;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SellerMapper {
    int insertSeller(Seller seller);

    Seller selectSellerById(String sellerId);

    String isSellerIdDuplicate(String sellerId);

    long findSellerNo(String companyName);
}
