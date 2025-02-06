package ru.practicum.closed.user.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.closed.user.event.model.Event;
import ru.practicum.closed.user.event.model.EventRequestStatusUpdateResult;
import ru.practicum.closed.user.event.model.UpdateEventUserRequest;
import ru.practicum.closed.user.event.repository.PrivateEventStorage;
import ru.practicum.closed.user.request.model.ParticipationRequest;
import ru.practicum.closed.user.request.service.PrivateParticipationRequestService;
import ru.practicum.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.state.State;
import ru.practicum.state_action.StateAction;
import ru.practicum.status.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PrivateUserEventService {
    private final PrivateEventStorage privateEventStorage;
    private final PrivateParticipationRequestService privateParticipationRequestService;

    public PrivateUserEventService(PrivateEventStorage privateEventStorage,
                                   PrivateParticipationRequestService privateParticipationRequestService) {
        this.privateEventStorage = privateEventStorage;
        this.privateParticipationRequestService = privateParticipationRequestService;
    }

    public List<Event> findAll(Long userId, Long from, Long size) {
        Pageable pageable = PageRequest.of(from.intValue(), size.intValue());
        return privateEventStorage.findAllById(userId, pageable);
    }

    public Event create(Event event) {
        return privateEventStorage.save(event);
    }

    public Event findByInitiatorIdAndEventId(Long userId, Long eventId) {
        return privateEventStorage.findByInitiatorIdAndEventId(userId, eventId);
    }

    public Event patchByUserIdAndEventId(Long userId, Long eventId, UpdateEventUserRequest patchEvent) {
        LocalDateTime now = LocalDateTime.now();
        Event byUserIdAndEventId = privateEventStorage.findByInitiatorIdAndEventId(userId, eventId);
        if (byUserIdAndEventId != null && byUserIdAndEventId.getEventDate().isAfter(now.plusHours(2)) &&
                (byUserIdAndEventId.getState().equals(State.PENDING) || byUserIdAndEventId.getState().equals(State.CANCELED))) {
            if (patchEvent.getAnnotation() != null) {
                byUserIdAndEventId.setAnnotation(patchEvent.getAnnotation());
            }
            if (patchEvent.getCategory() != null) {
                byUserIdAndEventId.setCategory(patchEvent.getCategory());
            }
            if (patchEvent.getDescription() != null) {
                byUserIdAndEventId.setDescription(patchEvent.getDescription());
            }
            if (patchEvent.getEventDate() != null) {
                byUserIdAndEventId.setEventDate(patchEvent.getEventDate());
            }
            if (patchEvent.getLocation() != null) {
                byUserIdAndEventId.setLocation(patchEvent.getLocation());
            }
            if (patchEvent.getPaid() != null) {
                byUserIdAndEventId.setPaid(patchEvent.getPaid());
            }
            if (patchEvent.getParticipantLimit() != null) {
                byUserIdAndEventId.setParticipantLimit(patchEvent.getParticipantLimit());
            }
            if (patchEvent.getRequestModeration() != null) {

                byUserIdAndEventId.setRequestModeration(patchEvent.getRequestModeration());
            }
            if (patchEvent.getStateAction() != null) {
                if (patchEvent.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                    byUserIdAndEventId.setState(State.CANCELED);
                } else if (patchEvent.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                    byUserIdAndEventId.setState(State.PENDING);
                }
            }
            return privateEventStorage.save(byUserIdAndEventId);
        } else {
            throw new RuntimeException("Conflict");
        }
    }

    public EventRequestStatusUpdateResult patchRequestsByUserIdAndEventId(Long userId, Long eventId,
                                                                          EventRequestStatusUpdateRequestDto patchRequest) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        int zero = 0;
        Event event = findByInitiatorIdAndEventId(userId, eventId);
        if (patchRequest != null && event != null) {
            ArrayList<Long> requestIds = patchRequest.getRequestIds();

            // Нет лимита заявок или отключена пре-модерация (не требуется подтверждение заявок)
            if (event.getParticipantLimit() == zero || !event.getRequestModeration()) {
                for (Long requestId : requestIds) {
                    ParticipationRequest request = privateParticipationRequestService.findRequestByInitiatorIdAndEventId
                            (requestId, event.getId());
                    if (request != null) {
                        request.setStatus(Status.CONFIRMED);
                        ParticipationRequest update = privateParticipationRequestService.update(requestId, request);
                        result.getConfirmedRequests().add(update);
                    }
                }
            }
            // Пре-модерация включена, лимит заявок > 0 (необходимо подтверждение заявок)
            else if (event.getParticipantLimit() > zero && event.getRequestModeration()) {
                int count = 0;
                for (Long requestId : requestIds) {
                    ParticipationRequest request = privateParticipationRequestService.findRequestByInitiatorIdAndEventId
                            (requestId, event.getId());

                    // Проверка: если достигнут лимит, выбрасываем исключение
                    if (count >= event.getParticipantLimit()) {
                        throw new ConflictException("Participant limit reached");
                    }

                    // Проверка: заявка должна быть в статусе PENDING
                    if (request == null || !request.getStatus().equals(Status.PENDING)) {
                        throw new BadRequestException("Request must have status PENDING");
                    }

                    request.setStatus(Status.CONFIRMED);
                    ParticipationRequest update = privateParticipationRequestService.update(requestId, request);
                    result.getConfirmedRequests().add(update);
                    count++;
                }

                // Если лимит заявок для события исчерпан, отклоняем все неподтвержденные заявки
                if (count >= event.getParticipantLimit()) {
                    // Отклоняем неподтвержденные заявки
                    for (Long requestId : requestIds) {
                        ParticipationRequest request = privateParticipationRequestService
                                .findRequestByInitiatorIdAndEventId(requestId, event.getId());
                        if (request != null && request.getStatus() == Status.PENDING) {
                            request.setStatus(Status.REJECTED);
                            ParticipationRequest update = privateParticipationRequestService.update(requestId, request);
                            result.getRejectedRequests().add(update);
                        }
                    }
                }
            }
        } else {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        return result;
    }
}
