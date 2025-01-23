package hello.fclover.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString
@Data
public class GoodsImage {
    private Long imageNo; // 이미지 키
    private Long goodsNo; // 상품 번호
    private String goodsImageName; // 이미지 이름
    private String goodsUrl; //이미지 저장 path
    private String isMain; //대표이미지 설정
}

