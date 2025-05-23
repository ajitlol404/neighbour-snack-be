package com.ecommerce.neighboursnackbe.controller;

import com.ecommerce.neighboursnackbe.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryDTO.CategoryRequestDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryDTO.CategoryResponseDTO;
import com.ecommerce.neighboursnackbe.dto.CategoryFilterRequest;
import com.ecommerce.neighboursnackbe.dto.PaginationResponse;
import com.ecommerce.neighboursnackbe.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static com.ecommerce.neighboursnackbe.util.AppConstant.ADMIN_BASE_API_PATH;

@RestController
@RequestMapping(ADMIN_BASE_API_PATH + "/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryRequestDTO requestDTO) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(requestDTO);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{uuid}")
                        .buildAndExpand(createdCategory.uuid())
                        .toUri()
        ).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<CategoryPublicResponseDTO>> getAllCategories(@Valid @ModelAttribute CategoryFilterRequest categoryFilterRequest) {
        return ResponseEntity.ok(categoryService.getAllCategories(categoryFilterRequest));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CategoryResponseDTO> getCategoryByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(categoryService.getCategoryByUuid(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable UUID uuid,
                                                              @RequestBody @Valid CategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(categoryService.updateCategoryByUuid(uuid, requestDTO));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID uuid) {
        categoryService.deleteCategoryByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryPublicResponseDTO>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

}
