package ru.practicum.open.category.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.open.category.mapper.CategoryMapper;
import ru.practicum.open.category.service.PublicCategoryService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
public class PublicCategoryController {
    private final PublicCategoryService publicCategoryService;

    public PublicCategoryController(PublicCategoryService publicCategoryService) {
        this.publicCategoryService = publicCategoryService;
    }

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0", required = false) Long from,
                                    @RequestParam(defaultValue = "10" , required = false) Long size) {
        return  publicCategoryService.findAllSortedAndSize(from, size).stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable(value = "catId") Long id) {
        return CategoryMapper.toCategoryDto(publicCategoryService.findById(id));
    }
}
