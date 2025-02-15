package ru.practicum.closed.user.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.closed.user.event.model.Event;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    @Query("SELECT e " +
            "FROM Event as e " +
            "WHERE e.id = :eventId")
    Event findByEventId(@Param("eventId") Long eventId);

    @Query("SELECT e " +
            "FROM Event as e " +
            "WHERE e.category.id =:categoryId")
    Event findByCategoryId(@Param("categoryId") long categoryId);

    @Query("SELECT e " +
            "FROM Event as e " +
            "WHERE e.id IN :eventIds ")
    List<Event> findEventsByEventIds(List<Long> eventIds);

    @Query("SELECT e " +
            "FROM Event as e " +
            "WHERE e.title = :title")
    List<Event> findEventsByTitle(String title);

    @Query("SELECT e FROM Event e " +
            "WHERE (:text IS NULL OR e.annotation ILIKE %:text% OR e.description ILIKE %:text%) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:rangeStart IS NULL OR e.eventDate > :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate < :rangeEnd) " +
            "AND (:onlyAvailable IS NULL OR e.participantLimit > 0) ")
    List<Event> findAllEventsNotSorted(@Param("text") String text,
                                           @Param("categories") List<Long> categories,
                                           @Param("paid") Boolean paid,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                           @Param("rangeEnd") LocalDateTime rangeEnd,
                                           @Param("onlyAvailable") Boolean onlyAvailable,
                                           Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (:text IS NULL OR e.annotation ILIKE %:text% OR e.description ILIKE %:text%) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:rangeStart IS NULL OR e.eventDate > :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate < :rangeEnd) " +
            "AND (:onlyAvailable IS NULL OR e.participantLimit > 0) " +
            "ORDER BY e.views ")
    List<Event> findAllEventsSortedByViews(@Param("text") String text,
                              @Param("categories") List<Long> categories,
                              @Param("paid") Boolean paid,
                              @Param("rangeStart") LocalDateTime rangeStart,
                              @Param("rangeEnd") LocalDateTime rangeEnd,
                              @Param("onlyAvailable") Boolean onlyAvailable,
                              Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (:text IS NULL OR e.annotation ILIKE %:text% OR e.description ILIKE %:text%) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:rangeStart IS NULL OR e.eventDate > :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate < :rangeEnd) " +
            "AND (:onlyAvailable IS NULL OR e.participantLimit > 0) " +
            "ORDER BY e.eventDate")
    List<Event> findAllEventsSortedByEventDate(@Param("text") String text,
                              @Param("categories") List<Long> categories,
                              @Param("paid") Boolean paid,
                              @Param("rangeStart") LocalDateTime rangeStart,
                              @Param("rangeEnd") LocalDateTime rangeEnd,
                              @Param("onlyAvailable") Boolean onlyAvailable,
                              Pageable pageable);

    @Query("SELECT e " +
            "FROM Event as e " +
            "WHERE e.eventDate > :rangeStart " +
            "ORDER BY e. eventDate ")
    List<Event> findAllCustom(@Param("rangeStart") LocalDateTime rangeStart);
}
