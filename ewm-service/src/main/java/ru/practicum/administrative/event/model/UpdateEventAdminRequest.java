package ru.practicum.administrative.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.model.Location;
import ru.practicum.open.category.model.Category;
import ru.practicum.state_action.StateAction;

import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {
    /*@Pattern(regexp = "^(?!,*$).+", message = "Field  annotation не может быть пустым или содержать один символ")
    @Size(min = 20, max = 2000, message = "Field annotation должно содержать min=20, max=7000 символов")*/
    private String annotation;
    private Long category;
    /*@Pattern(regexp = "^(?=.*\\S).*$", message = "Field description не может быть пустым или содержать только пробелы")
    @Size(min = 20, max = 7000, message = "Field description должно содержать min=20, max=7000 символов")*/
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    /*@Pattern(regexp = "^(?!,*$).+", message = "Field title не может быть пустым или содержать один символ")
    @Size(min = 3, max = 120, message = "Field annotation должно содержать min=20, max=7000 символов")*/
    private String title;
}
