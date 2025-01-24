package hello.fclover.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cateNo; // 카테고리 키
    private String cateName; // 카테고리 명
}
