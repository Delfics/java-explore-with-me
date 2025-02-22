package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {

    private List<Long> events;

    private Long id;

    private Boolean pinned;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50, message = "Field title должно содержать min=1, max=50 символов")
    private String title;
}
