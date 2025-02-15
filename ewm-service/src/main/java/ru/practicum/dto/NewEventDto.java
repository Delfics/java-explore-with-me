package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotBlank(message = "Field annotation не может быть пустым")
    @NotNull
    @Size(min = 20, max = 2000, message = "Field annotation должно содержать min=20, max=2000 символов")
    private String annotation;

    private Long category;

    @NotBlank(message = "Field description не может быть пустым")
    @NotNull
    @Size(min = 20, max = 7000, message = "Field description должно содержать min=20, max=7000 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    private Location location;

    private Boolean paid;

    private Long participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    @NotNull
    @Size(min = 3, max = 120, message = "Field annotation должно содержать min=3, max=120 символов")
    private String title;
}
