package com.ecommerce.neighboursnackbe.service.impl;

import com.ecommerce.neighboursnackbe.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryDTO.CategoryRequestDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryDTO.CategoryResponseDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryFilterRequest;
import com.ecommerce.neighboursnackbe.dto.PaginationResponse;
import com.ecommerce.neighboursnackbe.entity.Category;
import com.ecommerce.neighboursnackbe.exception.CategoryException;
import com.ecommerce.neighboursnackbe.repository.CategoryRepository;
import com.ecommerce.neighboursnackbe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "createdAt", "updatedAt");

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO createCategoryDTO) {

        // Check if category already exists
        if (categoryRepository.existsByName(createCategoryDTO.name().toLowerCase())) {
            throw new CategoryException("Category with this name already exists");
        }

        // Convert DTO to entity, set normalizedName, and save
        Category category = createCategoryDTO.toEntity();
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponseDTO.fromEntity(savedCategory);
    }

    @Override
    public PaginationResponse<CategoryPublicResponseDTO> getAllCategories(CategoryFilterRequest categoryFilterRequest) {

        String sortBy = ALLOWED_SORT_FIELDS.contains(categoryFilterRequest.sortBy()) ? categoryFilterRequest.sortBy() : "createdAt";

        // Sorting logic
        Sort sort = categoryFilterRequest.sortDir().equalsIgnoreCase("asc")
                ? Sort.by(categoryFilterRequest.sortBy()).ascending()
                : Sort.by(categoryFilterRequest.sortBy()).descending();

        // Pageable for pagination and sorting
        Pageable pageable = PageRequest.of(categoryFilterRequest.page(), categoryFilterRequest.size(), sort);

        Specification<Category> spec = CategorySpecifications.build(categoryFilterRequest);

        // Fetch filtered data with pagination and sorting
        Page<Category> categoryPage = categoryRepository.findAll(spec, pageable);

        // Map entities to response DTOs
        List<CategoryPublicResponseDTO> content = categoryPage.getContent()
                .stream()
                .map(CategoryPublicResponseDTO::fromEntity)
                .toList();

        return new PaginationResponse<>(
                content,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast(),
                categoryFilterRequest.sortBy(),
                categoryFilterRequest.sortDir()
        );

    }

    @Override
    public CategoryResponseDTO getCategoryByUuid(UUID uuid) {
        Category category = categoryRepository.findCategoryByUuid(uuid);
        return CategoryResponseDTO.fromEntity(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategoryByUuid(UUID uuid, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = categoryRepository.findCategoryByUuid(uuid);

        // Check if the name is being updated
        if (!existingCategory.getName().equalsIgnoreCase(categoryRequestDTO.name()) &&
                categoryRepository.existsByName(categoryRequestDTO.name().toLowerCase())) {
            throw new CategoryException("Category with this name already exists");
        }

        // Update and save the category in one step
        Category savedCategory = categoryRepository.save(categoryRequestDTO.updateCategory(existingCategory));

        return CategoryResponseDTO.fromEntity(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryByUuid(UUID uuid) {
        Category category = categoryRepository.findCategoryByUuid(uuid);
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryPublicResponseDTO> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(CategoryPublicResponseDTO::fromEntity)
                .toList();
    }

}
