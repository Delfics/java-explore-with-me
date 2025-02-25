package ru.practicum.open.category.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Category;

import java.util.List;

@Repository
public interface PublicCategoryStorage extends JpaRepository<Category, Long> {
    @Query("SELECT c " +
            "FROM Category as c ")
    List<Category> findAllSortedFromAndSize(Pageable pageable);

    @Query("SELECT COUNT(c) > 0 " +
            "FROM Category as c " +
            "WHERE c.id IN :categories")
    Boolean existsListCategoriesId(@Param("categories") List<Long> categories);
}
