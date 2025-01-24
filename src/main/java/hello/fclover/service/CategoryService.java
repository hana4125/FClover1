package hello.fclover.service;

import hello.fclover.domain.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getCategoryList();

    Category getCategoryByNo(int cateNo);
}


