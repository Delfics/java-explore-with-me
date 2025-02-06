package ru.practicum.administrative.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.NewEventDto;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/events")
public class AdminEventController {
    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<String> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd,
                                     @RequestParam(defaultValue = "0", required = false) Long from,
                                     @RequestParam(defaultValue = "10" , required = false) Long size) {
        return null;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId, @RequestBody NewEventDto newEventDto) {
        return null;
    }
}
