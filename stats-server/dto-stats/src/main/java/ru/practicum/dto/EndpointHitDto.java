package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class EndpointHitDto {
    @NotNull
    @Pattern(regexp = "^(?!,*$).+", message = "Название сервиса не может быть пустым или содержать один символ")
    private String app;
    @NotNull
    @Pattern(regexp = "^(?!,*$).+", message = "Uri не может быть пустым или содержать один символ")
    private String uri;
    @NotNull
    @Pattern(regexp = "^(?!,*$).+", message = "Ip не может быть пустым или содержать один символ")
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}
