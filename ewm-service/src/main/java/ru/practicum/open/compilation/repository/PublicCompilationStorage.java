package ru.practicum.open.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.open.compilation.model.Compilation;

import java.util.List;

public interface PublicCompilationStorage extends JpaRepository<Compilation, Long> {
    @Query("SELECT c " +
            "FROM Compilation as c " +
            "LEFT JOIN FETCH c.events " +
            "WHERE c.pinned = :pined ")
    List<Compilation> findAllPinedSortedFromAndSize(@Param("pined") Boolean pined, Pageable pageable);

    @Query("SELECT c " +
            "FROM Compilation as c " +
            "LEFT JOIN FETCH c.events ")
    List<Compilation> findAllSortedFromAndSize(Pageable pageable);

/*
    Compilation findCompilation
*/
}
