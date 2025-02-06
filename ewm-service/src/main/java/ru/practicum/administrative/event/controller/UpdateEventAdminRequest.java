package ru.practicum.administrative.event.controller;

import lombok.Data;
import ru.practicum.model.Location;
import ru.practicum.open.category.model.Category;
import ru.practicum.state_action.StateAction;

import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
