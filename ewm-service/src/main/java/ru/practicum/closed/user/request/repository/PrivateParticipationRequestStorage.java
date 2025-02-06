package ru.practicum.closed.user.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.closed.user.request.model.ParticipationRequest;

import java.util.List;

@Repository
public interface PrivateParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {
    @Query("SELECT prqst " +
            "FROM ParticipationRequest prqst " +
            "JOIN Event e ON prqst.event = e.id " +  // Соединяем с сущностью Event
            "WHERE prqst.requester = :userId " +     // Фильтруем по текущему пользователю
            "AND e.initiator.id != :userId")         // Исключаем заявки на события, инициированные этим пользователем
    List<ParticipationRequest> findAllRequestsForUserOnOtherEvents(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ParticipationRequest as prqst " +
            "WHERE prqst.id = :requestId " +
            "AND prqst.requester = :userId ")
    void deleteRequestByUserIdAndRequestId(@Param("userId") Long userId, @Param("requestId") Long requestId);

    @Query("SELECT prqst " +
            "FROM ParticipationRequest as prqst " +
            "JOIN Event e ON prqst.event = e.id " +
            "WHERE e.initiator.id = :initiatorId " +
            "AND e.id = :eventId")
    ParticipationRequest findRequestByInitiatorIdAndEventId(@Param("initiatorId") Long initiatorId,
                                                            @Param("eventId") Long eventId);
}
