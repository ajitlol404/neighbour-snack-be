package com.ecommerce.neighboursnackbe.service;

import com.ecommerce.neighboursnackbe.dto.CategoryDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryDTO.CategoryResponseDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryFilterRequest;
import com.ecommerce.neighboursnackbe.dto.PaginationResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryDTO.CategoryRequestDTO categoryRequestDTO);

    PaginationResponse<CategoryPublicResponseDTO> getAllCategories(CategoryFilterRequest categoryFilterRequest);

    CategoryResponseDTO getCategoryByUuid(UUID uuid);

    CategoryResponseDTO updateCategoryByUuid(UUID uuid, CategoryDTO.CategoryRequestDTO categoryRequestDTO);

    void deleteCategoryByUuid(UUID uuid);

    List<CategoryPublicResponseDTO> getActiveCategories();

}