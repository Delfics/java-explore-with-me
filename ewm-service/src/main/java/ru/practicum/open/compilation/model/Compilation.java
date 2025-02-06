package ru.practicum.open.compilation.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.dto.EventShortDto;

import java.util.List;

@Data
@Entity
public class Compilation {
    @Column(name = "events")
    private List<EventShortDto> events;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;
}
