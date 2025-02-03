package hello.fclover.dto;

import hello.fclover.domain.GoodsImage;
import lombok.Data;

@Data
public class WishDTO {

    private Long wishNo;
    private Long memberNo;
    private Long goodsNo;
    private String goodsName;
    private String goodsWriter;
    private GoodsImage mainImage;

}