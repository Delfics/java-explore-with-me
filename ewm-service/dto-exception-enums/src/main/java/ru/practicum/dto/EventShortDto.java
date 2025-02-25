package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventShortDto {

    @Size(min = 20, max = 2000, message = "Field annotation должно содержать min=20, max=2000 символов")
    @NotNull
    private String annotation;

    private NewCategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    @Size(min = 3, max = 120, message = "Field annotation должно содержать min=20, max=120 символов")
    @NotNull
    private String title;

    private Long views;
}
