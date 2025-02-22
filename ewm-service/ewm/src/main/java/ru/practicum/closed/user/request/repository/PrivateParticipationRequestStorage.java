package ru.practicum.closed.user.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;

import java.util.List;

@Repository
public interface PrivateParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {
    @Query("SELECT prqst " +
            "FROM ParticipationRequest prqst " +
            "JOIN Event e ON prqst.event = e.id " +
            "WHERE prqst.requester = :userId " +
            "AND e.initiator.id != :userId")
    List<ParticipationRequest> findAllRequestsForUserOnOtherEvents(@Param("userId") Long userId);

    @Query("SELECT prqst " +
            "FROM ParticipationRequest as prqst " +
            "JOIN Event e ON prqst.event = e.id " +
            "WHERE e.initiator.id = :initiatorId " +
            "AND e.id = :eventId")
    List<ParticipationRequest> findRequestsByInitiatorIdAndEventId(@Param("initiatorId") Long initiatorId,
                                                                   @Param("eventId") Long eventId);

    @Query("SELECT prqst " +
            "FROM ParticipationRequest as prqst " +
            "WHERE prqst.event = :eventId " +
            "AND prqst.requester = :requesterId ")
    ParticipationRequest findRequestByRequesterIdAndEventId(@Param("requesterId") Long requesterId,
                                                            @Param("eventId") Long eventId);

    @Query("SELECT prqst " +
            "FROM ParticipationRequest as prqst " +
            "WHERE prqst.id = :requestId " +
            "AND prqst.requester = :requesterId")
    ParticipationRequest findRequestByRequesterIdAndRequestId(@Param("requesterId") Long requesterId,
                                                              @Param("requestId") Long requestId);

    @Query("SELECT prqst " +
            "FROM ParticipationRequest as prqst " +
            "WHERE prqst.event = :eventId")
    List<ParticipationRequest> findRequestsByEventId(@Param("eventId") Long eventId);
}
