package hello.fclover.mybatis.mapper;


import hello.fclover.domain.Seller;
import hello.fclover.dto.SellerCompanyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SellerMapper {
    int insertSeller(Seller seller);

    Seller selectSellerById(String sellerId);

    String isSellerIdDuplicate(String sellerId);


    long findSellerNo(String companyName);


    List<Map<String, Object>> getListDetail(Map<String, Object> params);
    int getListCount(Map<String, Object> params);

    int getSearchListCount(Map<String, Object> map);
    List<Seller> getSearchList(Map<String, Object> map);

    long getselectNo(String sellerId);

    int countAllSeller();

    void insertSellers(@Param("sellers") List<Seller> sellers);

    List<SellerCompanyDTO> getSellerCompanyName();
}
