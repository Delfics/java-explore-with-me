package ru.practicum.administrative.category.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.administrative.category.service.AdminCategoryService;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.open.category.mapper.CategoryMapper;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public NewCategoryDto create(@RequestBody @Valid NewCategoryDto categoryDto) {
        log.info("Create category: {}", categoryDto);
        return CategoryMapper.toCategoryDto(adminCategoryService.createCategory(CategoryMapper
                .toCategory(categoryDto)));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(value = "catId") Long id) {
        log.info("Delete category: {}", id);
        adminCategoryService.deleteCategory(id);
    }

    @PatchMapping("/{catId}")
    public NewCategoryDto update(@PathVariable(value = "catId") Long id, @RequestBody @Valid NewCategoryDto category) {
        log.info("Update category: {}", category);
        return CategoryMapper.toCategoryDto(adminCategoryService.updateCategory(id,
                CategoryMapper.toCategory(category)));
    }
}
