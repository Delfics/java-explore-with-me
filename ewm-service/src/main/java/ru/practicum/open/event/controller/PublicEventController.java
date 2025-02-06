package ru.practicum.open.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventShortDto;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
public class PublicEventController {
    @GetMapping
    public EventShortDto getAll(@RequestParam(required = false) String text,
                                @RequestParam(required = false) List<Long> categories,
                                @RequestParam(required = false) Boolean paid,
                                @RequestParam(required = false) String rangeStart,
                                @RequestParam(required = false) String rangeEnd,
                                @RequestParam(required = false) Boolean onlyAvailable,
                                @RequestParam(required = false) String sort,
                                @RequestParam(defaultValue = "0",required = false) String from,
                                @RequestParam(defaultValue = "10", required = false) Integer size) {
        return null;

    }

    @GetMapping("/{id}")
    public EventShortDto getById(@PathVariable Long id) {
        return null;
    }
}
