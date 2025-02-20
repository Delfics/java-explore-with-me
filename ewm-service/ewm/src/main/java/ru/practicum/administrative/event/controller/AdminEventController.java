package ru.practicum.administrative.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.administrative.event.model.UpdateEventAdminRequest;
import ru.practicum.administrative.event.service.AdminEventService;
import ru.practicum.closed.user.event.mapper.EventMapper;
import ru.practicum.dto.EventFullDto;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;

    public AdminEventController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<String> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd,
                                     @RequestParam(defaultValue = "0", required = false) Integer from,
                                     @RequestParam(defaultValue = "10", required = false) Integer size) {
        return adminEventService.findAll(users, states, categories, rangeStart, rangeEnd,
                        from, size).stream()
                .map(EventMapper::toEventFullDto)
                .toList();
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patch(@PathVariable Long eventId,
                              @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        adminEventService.validateUpdateEventAdminRequest(updateEventAdminRequest);
        return EventMapper.toEventFullDto(adminEventService.updateEventAdminRequest(eventId, updateEventAdminRequest));
    }
}
