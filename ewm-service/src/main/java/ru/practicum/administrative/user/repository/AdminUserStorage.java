package ru.practicum.administrative.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.administrative.user.model.User;

import java.util.List;

@Repository
public interface AdminUserStorage extends JpaRepository<User, Long> {
    @Query("SELECT u " +
            "FROM User as u " +
            "WHERE u.id IN :ids ")
    List<User> findAllByIdsAndFromAndSize(@Param("ids") List<Long> ids, Pageable pageable);

    @Query("SELECT u " +
            "FROM User as u ")
    List<User> findAllByFromAndSize(Pageable pageable);

    boolean existsByEmail(String email);
}
