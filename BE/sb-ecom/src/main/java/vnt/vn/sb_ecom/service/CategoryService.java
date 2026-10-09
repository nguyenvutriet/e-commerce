package vnt.vn.sb_ecom.service;

import vnt.vn.sb_ecom.model.Category;
import vnt.vn.sb_ecom.payload.CategoryDTO;
import vnt.vn.sb_ecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
