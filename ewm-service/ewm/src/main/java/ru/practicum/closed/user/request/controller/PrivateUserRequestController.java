package ru.practicum.closed.user.request.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.closed.user.request.mapper.ParticipationRequestMapper;
import ru.practicum.closed.user.request.service.PrivateParticipationRequestService;
import ru.practicum.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class PrivateUserRequestController {
    private final PrivateParticipationRequestService privateParticipationRequestService;

    PrivateUserRequestController(PrivateParticipationRequestService privateParticipationRequestService) {
        this.privateParticipationRequestService = privateParticipationRequestService;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllRequestsByUserId(@PathVariable("userId") Long userId) {
        return privateParticipationRequestService.findAllForUserOnOtherEvents(userId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequestByUserId(@PathVariable("userId") Long userId,
                                                         @RequestParam("eventId") Long eventId) {
        return ParticipationRequestMapper.toParticipationRequestDto(privateParticipationRequestService
                .create(userId, eventId));

    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto patchRequestCancel(@PathVariable("userId") Long userId,
                                                      @PathVariable("requestId") Long requestId) {
        return ParticipationRequestMapper.toParticipationRequestDto(privateParticipationRequestService
                .cancelRequest(userId, requestId));
    }
}
