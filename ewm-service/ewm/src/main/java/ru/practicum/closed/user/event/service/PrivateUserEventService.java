package ru.practicum.closed.user.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.closed.user.event.repository.CommentStorage;
import ru.practicum.dto.*;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.*;
import ru.practicum.administrative.user.service.AdminUserService;
import ru.practicum.mapper.EventMapper;
import ru.practicum.closed.user.event.repository.PrivateEventStorage;
import ru.practicum.closed.user.request.service.PrivateParticipationRequestService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.open.category.service.PublicCategoryService;
import ru.practicum.enums.State;
import ru.practicum.enums.StateAction;
import ru.practicum.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PrivateUserEventService {
    private final PrivateEventStorage privateEventStorage;
    private final PrivateParticipationRequestService privateParticipationRequestService;
    private final AdminUserService adminUserService;
    private final PublicCategoryService publicCategoryService;
    private final CommentStorage commentStorage;

    public PrivateUserEventService(PrivateEventStorage privateEventStorage,
                                   PrivateParticipationRequestService privateParticipationRequestService,
                                   AdminUserService adminUserService,
                                   PublicCategoryService publicCategoryService,
                                   CommentStorage commentStorage) {
        this.privateEventStorage = privateEventStorage;
        this.privateParticipationRequestService = privateParticipationRequestService;
        this.adminUserService = adminUserService;
        this.publicCategoryService = publicCategoryService;
        this.commentStorage = commentStorage;
    }

    public List<Event> findAll(Long userId, Long from, Long size) {
        Pageable pageable = PageRequest.of(from.intValue(), size.intValue());
        return privateEventStorage.findAllById(userId, pageable);
    }

    @Transactional
    public Event create(Long userId, NewEventDto newEventDto) {
        User initiator = adminUserService.findById(userId);
        Category category = publicCategoryService.findById(newEventDto.getCategory());
        Event event = EventMapper.toEvent(initiator, category, newEventDto);
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Field: eventDate. Error: должно содержать дату, которая еще не наступила.");
        } else {
            log.debug("Creating event {}", event);
            event.setInitiator(initiator);
            if (newEventDto.getPaid() == null) {
                event.setPaid(false);
            }
            if (newEventDto.getParticipantLimit() == null) {
                event.setParticipantLimit(0L);
            }
            if (newEventDto.getParticipantLimit() != null) {
                if (newEventDto.getParticipantLimit() < 0) {
                    throw new BadRequestException("Field: participantLimit must not be negative.");
                }
            }
            if (newEventDto.getRequestModeration() == null) {
                event.setRequestModeration(true);
            }
            return privateEventStorage.save(event);
        }
    }

    public Event findByEventId(Long eventId) {
        Event event = privateEventStorage.findByEventId(eventId);
        if (event == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        } else {
            return event;
        }
    }

    public Event findByInitiatorIdAndEventId(Long userId, Long eventId) {
        log.debug("Finding event by userId {}, eventId {}", userId, eventId);
        Event event = privateEventStorage.findByInitiatorIdAndEventId(userId, eventId);
        if (event == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        } else {
            return event;
        }
    }

    public Event patchByUserIdAndEventId(Long userId, Long eventId, UpdateEventUserRequest patchEvent) {

        Event byUserIdAndEventId = privateEventStorage.findByInitiatorIdAndEventId(userId, eventId);
        if (byUserIdAndEventId == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (byUserIdAndEventId.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Event date cannot be changing, because date time of updating must be earlier" +
                    "of " + byUserIdAndEventId.getEventDate().plusHours(2));
        }
        if (patchEvent.getEventDate() != null) {
            if (patchEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Event date cannot be changing, because date time of updating must be earlier");
            }
            byUserIdAndEventId.setEventDate(patchEvent.getEventDate());
        }
        if (byUserIdAndEventId.getState().equals(State.PENDING) ||
                byUserIdAndEventId.getState().equals(State.CANCELED)) {
            if (patchEvent.getAnnotation() != null) {
                byUserIdAndEventId.setAnnotation(patchEvent.getAnnotation());
            }
            if (patchEvent.getCategory() != null) {
                byUserIdAndEventId.setCategory(patchEvent.getCategory());
            }
            if (patchEvent.getDescription() != null) {
                byUserIdAndEventId.setDescription(patchEvent.getDescription());
            }
            if (patchEvent.getLocation() != null) {
                byUserIdAndEventId.setLocation(patchEvent.getLocation());
            }
            if (patchEvent.getPaid() != null) {
                byUserIdAndEventId.setPaid(patchEvent.getPaid());
            }
            if (patchEvent.getParticipantLimit() != null) {
                if (patchEvent.getParticipantLimit() < 0) {
                    throw new BadRequestException("Field: participantLimit must not be negative.");
                }
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
            throw new ConflictException("Only pending or canceled events can be changed");
        }
    }

    public EventRequestStatusUpdateResult patchRequestsByUserIdAndEventId(Long userId, Long eventId,
                                                                          EventRequestStatusUpdateRequestDto patchRequest) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        int zero = 0;
        Event event = findByInitiatorIdAndEventId(userId, eventId);
        if (patchRequest != null && event != null) {
            if (event.getParticipantLimit() > 0) {
                if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                    throw new ConflictException("Participant limit is full");
                }
            }
            ArrayList<Long> requestIds = patchRequest.getRequestIds();
            if (event.getParticipantLimit() == zero || !event.getRequestModeration()) {
                for (Long requestId : requestIds) {
                    ParticipationRequest request = privateParticipationRequestService.findById(requestId);
                    if (request != null && request.getId().equals(requestId)) {
                        request.setStatus(Status.CONFIRMED);
                        ParticipationRequest update = privateParticipationRequestService.update(request);
                        result.getConfirmedRequests().add(update);
                    } else {
                        throw new NotFoundException("Request with id=" + requestId + " was not found");
                    }
                }
            } else if (event.getParticipantLimit() > zero) {
                int count = 0;
                for (Long requestId : requestIds) {
                    ParticipationRequest request = privateParticipationRequestService.findById(
                            requestId);

                    if (count >= event.getParticipantLimit()) {
                        throw new ConflictException("Participant limit reached");
                    }

                    if (request == null || !request.getStatus().equals(Status.PENDING)) {
                        throw new BadRequestException("Request must have status PENDING");
                    }

                    if (patchRequest.getStatus().equals(Status.REJECTED)) {
                        request.setStatus(Status.REJECTED);
                        ParticipationRequest update = privateParticipationRequestService.update(request);
                        result.getRejectedRequests().add(update);
                    } else if (patchRequest.getStatus().equals(Status.CONFIRMED)) {
                        request.setStatus(Status.CONFIRMED);
                        ParticipationRequest update = privateParticipationRequestService.update(request);
                        result.getConfirmedRequests().add(update);
                    }
                    count++;
                }

                if (count >= event.getParticipantLimit()) {
                    for (Long requestId : requestIds) {
                        ParticipationRequest request = privateParticipationRequestService
                                .findById(requestId);
                        if (request != null && request.getStatus() == Status.PENDING) {
                            request.setStatus(Status.REJECTED);
                            ParticipationRequest update = privateParticipationRequestService.update(request);
                            result.getRejectedRequests().add(update);
                        }
                    }
                }
            }
        } else {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        Long size = (long) result.getConfirmedRequests().size();
        event.setConfirmedRequests(size);
        privateEventStorage.save(event);
        return result;
    }

    public Comment createComment(Comment comment) {
        Comment savedComment = commentStorage.save(comment);
        log.debug("Комментарий успешно создан {}", savedComment.getId());
        return savedComment;

    }

    public EventWithCommentsDto findEventWithCommentsByEventId(Long eventId) {
        Event byEventId = findByEventId(eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(byEventId);
        List<Comment> commentsByEventId = commentStorage.findCommentsByEventId(eventId);
        List<CommentDtoRequired> list = commentsByEventId.stream()
                .map(CommentMapper::toDto)
                .toList();
        return EventMapper.toEventWithCommentsDto(eventFullDto, list);
    }

    public void deleteCommentById(Long userId, Long commentId) {
        Optional<Comment> byId = commentStorage.findById(commentId);
        if (byId.isPresent()) {
            if (byId.get().getUser().getId().equals(userId)) {
                commentStorage.delete(byId.get());
            } else {
                throw new BadRequestException("Вы не являетесь пользователем который оставил комментарий");
            }
        } else {
            throw new NotFoundException("Comment with id=" + commentId + " не найден");
        }
    }

    public Comment patchCommentById(CommentDto comment, Long userId, Long commentId) {
        Optional<Comment> byId = commentStorage.findById(commentId);
        if (byId.isPresent()) {
            if (byId.get().getUser().getId().equals(userId)) {
                byId.get().setText(comment.getText());
                return commentStorage.save(byId.get());
            } else {
                throw new BadRequestException("Вы не являетесь пользователем который оставил комментарий");
            }
        } else {
            throw new NotFoundException("Comment with id=" + commentId + " не найден");
        }
    }

    public CommentDto addAuthorToCommentDto(CommentDto commentDto, Long userId) {
        User byId = adminUserService.findById(userId);
        commentDto.setAuthor(UserMapper.toUserDto(byId));
        log.debug("Добавил author в commentDto");
        return commentDto;
    }

    public CommentDto addEventToCommentDto(CommentDto commentDto, Long eventId) {
        Event byId = findByEventId(eventId);
        commentDto.setEvent(EventMapper.toEventFullDto(byId));
        log.debug("Добавил event в commentDto");
        return commentDto;
    }
}
