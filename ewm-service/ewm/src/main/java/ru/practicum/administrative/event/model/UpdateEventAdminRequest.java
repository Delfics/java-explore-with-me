package ru.practicum.administrative.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.model.Location;
import ru.practicum.enums.StateAction;

import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {

    private String annotation;

    private Long category;


    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    private String title;
}
