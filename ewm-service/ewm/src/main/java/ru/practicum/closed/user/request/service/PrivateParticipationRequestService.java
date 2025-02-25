package ru.practicum.closed.user.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.model.Event;
import ru.practicum.closed.user.event.repository.PrivateEventStorage;
import ru.practicum.closed.user.event.service.PrivateUserEventService;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.closed.user.request.repository.PrivateParticipationRequestStorage;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class PrivateParticipationRequestService {
    private final PrivateEventStorage privateEventStorage;
    private final PrivateUserEventService privateUserEventService;
    private final PrivateParticipationRequestStorage privateParticipationRequestStorage;

    PrivateParticipationRequestService(PrivateParticipationRequestStorage privateParticipationRequestStorage,
                                       @Lazy PrivateUserEventService privateUserEventService,
                                       PrivateEventStorage privateEventStorage) {
        this.privateParticipationRequestStorage = privateParticipationRequestStorage;
        this.privateUserEventService = privateUserEventService;
        this.privateEventStorage = privateEventStorage;
    }

    public List<ParticipationRequest> findAllForUserOnOtherEvents(Long userId) {
        return privateParticipationRequestStorage.findAllRequestsForUserOnOtherEvents(userId);
    }

    public ParticipationRequest findById(Long requestId) {
        Optional<ParticipationRequest> byId = privateParticipationRequestStorage.findById(requestId);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new NotFoundException("Request not found");
        }
    }

    public ParticipationRequest create(Long userId, Long eventId) {
        Event event = privateUserEventService.findByEventId(eventId);
        if (event == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        ParticipationRequest request = privateParticipationRequestStorage
                .findRequestByRequesterIdAndEventId(userId, eventId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Initiator can't make request in himself Event");
        }
        if (request != null) {
            throw new ConflictException("Request already exists");
        }
        if (event.getState().equals(State.PENDING) || event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Event canceled or wasn't published");
        }
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setStatus(Status.PENDING);
        if (!event.getRequestModeration()) {
            participationRequest.setStatus(Status.CONFIRMED);
        }
        if (event.getParticipantLimit() > 0) {
            if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                throw new ConflictException("Request limit exceeded");
            }
        }
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setEvent(eventId);
        participationRequest.setRequester(userId);
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0L)) {
            participationRequest.setStatus(Status.CONFIRMED);
            privateParticipationRequestStorage.save(participationRequest);
            Long confirmedRequests = event.getConfirmedRequests();
            confirmedRequests = confirmedRequests + 1L;
            event.setConfirmedRequests(confirmedRequests);
            privateEventStorage.save(event);
        }
        return privateParticipationRequestStorage.save(participationRequest);
    }


    public List<ParticipationRequest> findRequestsByInitiatorIdAndEventId(Long initiatorId, Long eventId) {
        return privateParticipationRequestStorage.findRequestsByInitiatorIdAndEventId(initiatorId, eventId);
    }

    public ParticipationRequest update(ParticipationRequest participationRequest) {
        return privateParticipationRequestStorage.save(participationRequest);
    }

    public ParticipationRequest cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = privateParticipationRequestStorage
                .findRequestByRequesterIdAndRequestId(userId, requestId);
        if (request == null) {
            throw new NotFoundException("Request with id=" + requestId + " was not found");
        }
        if (request.getStatus().equals(Status.PENDING)) {
            request.setStatus(Status.CANCELED);
        }
        return privateParticipationRequestStorage.save(request);
    }
}
