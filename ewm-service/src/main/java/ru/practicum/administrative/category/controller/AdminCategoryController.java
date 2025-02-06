package ru.practicum.administrative.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto category) {
        //Имя категории должно быть уникальным
        return category;
    }

    @DeleteMapping("/{catId}")
    public void deleteById(@PathVariable(value = "catId") Long id) {
        return;
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable(value = "catId") Long id, @RequestBody CategoryDto category) {
        return category;
    }
}
