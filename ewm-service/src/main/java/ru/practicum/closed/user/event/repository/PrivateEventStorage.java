package ru.practicum.closed.user.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.closed.user.event.model.Event;

import java.util.List;

@Repository
public interface PrivateEventStorage extends JpaRepository<Event, Long> {
    @Query("SELECT e " +
            "FROM Event as e " +
            "JOIN FETCH e.category " +
            "JOIN FETCH e.initiator " +
            "WHERE e.initiator.id = :userId")
    List<Event> findAllById(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event as e " +
            "JOIN FETCH e.category " +
            "JOIN FETCH e.initiator " +
            "JOIN FETCH e.location " +
            "WHERE e.initiator.id = :userId " +
            "AND e.id = :eventId ")
    Event findByInitiatorIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
