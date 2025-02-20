package ru.practicum.open.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.open.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public NewCategoryDto toCategoryDto(Category category) {
        NewCategoryDto dto = new NewCategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public Category toCategory(NewCategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}
