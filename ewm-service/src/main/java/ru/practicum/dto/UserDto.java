package ru.practicum.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    @Size(min = 2, max = 250, message = "Field name должно содержать min=2, max=250 символов")
    private String name;
    @Size(min = 6, max = 254, message = "Field email должно содержать min=6, max=254 символов")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]{1,59}@[A-Za-z0-9.-]{1,63}([.][A-Za-z0-9.-]{1,63})*$", message = "Email должен быть в формате")
    private String email;
}
