package ru.practicum.closed.user.request.controller;


import lombok.extern.slf4j.Slf4j;
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
                .map(ParticipationRequestMapper::toDto)
                .toList();
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequestByUserId(@PathVariable("userId") Long userId,
                                                         @RequestBody ParticipationRequestDto requestDto) {
        return ParticipationRequestMapper.toDto(privateParticipationRequestService
                .create(ParticipationRequestMapper.fromDto(requestDto)));

    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public void patchRequestCancel(@PathVariable("userId") Long userId, @PathVariable("requestId") Long requestId) {
        privateParticipationRequestService.delete(userId, requestId);
    }
}
