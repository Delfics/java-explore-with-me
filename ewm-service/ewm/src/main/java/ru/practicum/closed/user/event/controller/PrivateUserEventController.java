package ru.practicum.closed.user.event.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.model.EventRequestStatusUpdateResult;
import ru.practicum.model.UpdateEventUserRequest;
import ru.practicum.closed.user.event.service.PrivateUserEventService;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.closed.user.request.service.PrivateParticipationRequestService;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.dto.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class PrivateUserEventController {
    private final PrivateUserEventService privateUserEventService;
    private final PrivateParticipationRequestService privateParticipationRequestService;

    public PrivateUserEventController(PrivateUserEventService privateUserEventService,
                                      PrivateParticipationRequestService privateParticipationRequestService) {
        this.privateUserEventService = privateUserEventService;
        this.privateParticipationRequestService = privateParticipationRequestService;
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllEventsByUserId(@PathVariable("userId") Long userId,
                                                    @RequestParam(defaultValue = "0", required = false) Long from,
                                                    @RequestParam(defaultValue = "10", required = false) Long size) {
        return privateUserEventService.findAll(userId, from, size).stream()
                .map(EventMapper::toEventShortDto)
                .toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventFullDto createEventByUserId(@PathVariable("userId") Long userId,
                                            @Valid @RequestBody NewEventDto newEventDto) {
        return EventMapper.toEventFullDto(privateUserEventService.create(userId, newEventDto));
    }

    @GetMapping("{userId}/events/{eventId}")
    public EventFullDto getEventByUserIdAndEventId(@PathVariable("userId") Long userId,
                                                   @PathVariable("eventId") Long eventId) {
        Event byInitiatorIdAndEventId = privateUserEventService.findByInitiatorIdAndEventId(userId, eventId);
        return EventMapper.toEventFullDto(byInitiatorIdAndEventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto patchEventByUserIdAndEventId(@PathVariable("userId") Long userId,
                                                     @PathVariable("eventId") Long eventId,
                                                     @RequestBody @Valid UpdateEventUserRequest patchEvent) {
        return EventMapper.toEventFullDto(privateUserEventService.patchByUserIdAndEventId(userId, eventId, patchEvent));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsByInitiatorIdAndEventId(@PathVariable("userId")
                                                                                         Long initiatorId,
                                                                                         @PathVariable("eventId")
                                                                                         Long eventId) {
        return privateParticipationRequestService.findRequestsByInitiatorIdAndEventId(initiatorId, eventId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .toList();
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequestsByInitiatorIdAndEventId(@PathVariable("userId") Long initiatorId,
                                                                               @PathVariable("eventId") Long eventId,
                                                                               @RequestBody EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto) {
        return privateUserEventService.patchRequestsByUserIdAndEventId(
                initiatorId, eventId, eventRequestStatusUpdateRequestDto);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/comments/{eventId}")
    public CommentDtoRequired createComment(@RequestBody CommentDto commentDto,  @PathVariable("userId") Long userId,
                                            @PathVariable("eventId") Long eventId) {
        CommentDto commentDto1 = privateUserEventService.addAuthorToCommentDto(commentDto, userId);
        commentDto1 = privateUserEventService.addEventToCommentDto(commentDto1, eventId);
        return CommentMapper.toDto(privateUserEventService.createComment(CommentMapper.toComment(commentDto1), userId,
                eventId));
    }

    @GetMapping("/comments/{eventId}")
    public EventWithCommentsDto findByEventWithComments(@PathVariable Long eventId) {
        return privateUserEventService.findEventWithCommentsByEventId(eventId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        privateUserEventService.deleteCommentById(userId, commentId);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentDtoRequired patchComment(@RequestBody CommentDto commentDto, @PathVariable Long userId,
                                           @PathVariable Long commentId) {
        return CommentMapper.toDto(privateUserEventService.patchCommentById(commentDto, userId, commentId));
    }
}
