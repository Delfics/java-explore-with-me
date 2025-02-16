package ru.practicum.administrative.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.open.category.model.Category;
import ru.practicum.open.category.repository.PublicCategoryStorage;

import java.util.Optional;

@Slf4j
@Service
public class AdminCategoryService {
    private final PublicCategoryStorage publicCategoryStorage;

    public AdminCategoryService(PublicCategoryStorage publicCategoryStorage) {
        this.publicCategoryStorage = publicCategoryStorage;
    }

    public Category createCategory(Category category) {
        log.debug("Creating new category");
        return publicCategoryStorage.save(category);
    }

    public void deleteCategory(Long catId) {
        log.debug("Deleting category with id {}", catId);
        Optional<Category> byId = publicCategoryStorage.findById(catId);
        if (byId.isPresent()) {
            log.debug("Deleting category with id {}", catId);
            publicCategoryStorage.deleteById(catId);
        } else {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
    }

    public Category updateCategory(Long catId, Category category) {
        Optional<Category> byId = publicCategoryStorage.findById(catId);
        if (byId.isPresent()) {
            log.debug("Updating category with id {}", catId);
            byId.get().setName(category.getName());
            return publicCategoryStorage.save(byId.get());
        } else {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
    }
}
