package ru.practicum.administrative.compilation.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    private List<Long> events;
    private Long id;
    private Boolean pinned;
/*    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?!,*$).+", message = "Title не может быть пустым или содержать один символ")
    @Size(min = 1, max = 50, message = "Field title должно содержать min=1, max=50 символов")*/
    private String title;
}
