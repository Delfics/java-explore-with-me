package ru.practicum.open.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.open.event.service.PublicEventService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(required = false) Boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0", required = false) Integer from,
                                      @RequestParam(defaultValue = "10", required = false) Integer size,
                                      HttpServletRequest request) throws Exception {
        return publicEventService.findAllEvents(text, categories, paid, rangeStart, rangeEnd,
                        onlyAvailable, sort, from, size, request).stream()
                .map(EventMapper::toEventShortDto)
                .toList();
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable Long eventId, HttpServletRequest request) throws Exception {
        return EventMapper.toEventFullDto(publicEventService.getEventById(eventId, request));
    }
}
