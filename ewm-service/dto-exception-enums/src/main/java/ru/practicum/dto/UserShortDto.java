package ru.practicum.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserShortDto {
    private Long id;

    @Size(min = 2, max = 250, message = "Field name должно содержать min=2, max=250 символов")
    private String name;
}
