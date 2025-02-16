package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewCategoryDto {

    private Long id;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?!,*$).+", message = "Имя не может быть пустым или содержать один символ")
    @Size(min = 1, max = 50, message = "Field annotation должно содержать min=1, max=50 символов")
    private String name;
}
