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


    List<Map<String, Object>> getListDetail(Map<String, Object> params);
    int getListCount(String searchWord);

    int getSearchListCount(Map<String, Object> map);
    List<Seller> getSearchList(Map<String, Object> map);

    int countAllSeller();

    void insertSellers(@Param("sellers") List<Seller> sellers);

    List<SellerCompanyDTO> getSellerCompanyName();
}
