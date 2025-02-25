package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    Long id;
    Long eventId;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 2000, message = "Field text должно содержать min=1, max=2000 символов")
    String text;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    EventFullDto event;
    UserDto author;
}
