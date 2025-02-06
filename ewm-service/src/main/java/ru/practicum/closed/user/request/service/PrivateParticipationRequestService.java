package ru.practicum.closed.user.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.closed.user.request.model.ParticipationRequest;
import ru.practicum.closed.user.request.repository.PrivateParticipationRequestStorage;

import java.util.List;

@Service
@Slf4j
public class PrivateParticipationRequestService {
    private final PrivateParticipationRequestStorage privateParticipationRequestStorage;

    PrivateParticipationRequestService(PrivateParticipationRequestStorage privateParticipationRequestStorage) {
        this.privateParticipationRequestStorage = privateParticipationRequestStorage;
    }

    public List<ParticipationRequest> findAllForUserOnOtherEvents(Long userId) {
        return privateParticipationRequestStorage.findAllRequestsForUserOnOtherEvents(userId);
    }

    public ParticipationRequest create(ParticipationRequest participationRequest) {
        return privateParticipationRequestStorage.save(participationRequest);
    }

    public void delete(Long userId, Long requestId) {
        privateParticipationRequestStorage.deleteRequestByUserIdAndRequestId(userId, requestId);
    }

    public ParticipationRequest findRequestByInitiatorIdAndEventId(Long initiatorId, Long eventId) {
        return privateParticipationRequestStorage.findRequestByInitiatorIdAndEventId(initiatorId, eventId);
    }

    public ParticipationRequest update(Long requestId, ParticipationRequest participationRequest) {
        return privateParticipationRequestStorage.save(participationRequest);
    }
}
