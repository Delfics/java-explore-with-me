package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.enums.State;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@Data
public class EventFullDto {

    @NotNull
    @Size(min = 20, max = 2000, message = "Field annotation должно содержать min=20, max=2000 символов")
    private String annotation;

    @NotNull
    private NewCategoryDto category;

    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @NotNull
    @Size(min = 20, max = 7000, message = "Field description должно содержать min=20, max=7000 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;

    @NotNull
    private Boolean paid;

    @NotNull
    private Long participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @NotNull
    private Boolean requestModeration;
    private State state;

    @NotNull
    @Size(min = 3, max = 120, message = "Field annotation должно содержать min=3, max=120 символов")
    private String title;
    private Long views;
}
