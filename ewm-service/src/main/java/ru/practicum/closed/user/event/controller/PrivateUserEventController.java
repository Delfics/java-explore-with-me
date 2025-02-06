package ru.practicum.closed.user.event.controller;

import com.sun.jdi.request.EventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.closed.user.event.mapper.EventMapper;
import ru.practicum.closed.user.event.model.EventRequestStatusUpdateResult;
import ru.practicum.closed.user.event.model.UpdateEventUserRequest;
import ru.practicum.closed.user.event.service.PrivateUserEventService;
import ru.practicum.closed.user.request.mapper.ParticipationRequestMapper;
import ru.practicum.closed.user.request.service.PrivateParticipationRequestService;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.ParticipationRequestDto;

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

    @PostMapping("/{userId}/events")
    public EventFullDto createEventByUserId(@PathVariable("userId") Long userId, @RequestBody EventFullDto event) {
        return EventMapper.toEventFullDto(privateUserEventService.create(EventMapper.toEvent(event)));
    }

    @GetMapping("{userId}/events/{eventId}")
    public EventFullDto getEventByUserIdAndEventId(@PathVariable("userId") Long userId,
                                                   @PathVariable("eventId") Long eventId) {
        return EventMapper.toEventFullDto(privateUserEventService.findByInitiatorIdAndEventId(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto patchEventByUserIdAndEventId(@PathVariable("userId") Long userId,
                                                     @PathVariable("eventId") Long eventId,
                                                     @RequestBody UpdateEventUserRequest patchEvent) {
        return EventMapper.toEventFullDto(privateUserEventService.patchByUserIdAndEventId(userId, eventId, patchEvent));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ParticipationRequestDto getParticipationRequestsByInitiatorIdAndEventId(@PathVariable("userId") Long initiatorId,
                                                                                   @PathVariable("eventId") Long eventId) {
        return ParticipationRequestMapper.toDto(privateParticipationRequestService
                .findRequestByInitiatorIdAndEventId(initiatorId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequestsByUserIdAndEventId(@PathVariable("userId") Long userId,
                                                                          @PathVariable("eventId") Long eventId,
                                                                          @RequestBody EventRequestStatusUpdateRequestDto
                                                            eventRequestStatusUpdateRequestDto) {
        return privateUserEventService.patchRequestsByUserIdAndEventId
                (userId, eventId, eventRequestStatusUpdateRequestDto);
    }
}
