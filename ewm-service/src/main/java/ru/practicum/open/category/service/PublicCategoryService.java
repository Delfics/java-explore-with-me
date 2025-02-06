package ru.practicum.open.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.open.category.model.Category;
import ru.practicum.open.category.repository.PublicCategoryStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PublicCategoryService {
    private final PublicCategoryStorage publicCategoryStorage;

    public PublicCategoryService(PublicCategoryStorage publicCategoryStorage) {
        this.publicCategoryStorage = publicCategoryStorage;
    }

    public List<Category> findAllSortedAndSize(Long from, Long size) {
        Pageable pageable = PageRequest.of(from.intValue(), size.intValue());
       return publicCategoryStorage.findAllSortedFromAndSize(pageable);
    }

    public Category findById(Long id) {
        Optional<Category> byId = publicCategoryStorage.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new NotFoundException("Category with id=" + id + " was not found");
        }
    }
}
