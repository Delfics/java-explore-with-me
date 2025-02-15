package ru.practicum.open.compilation.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.closed.user.event.model.Event;

import java.util.List;

@Data
@Entity
public class Compilation {
    @OneToMany(mappedBy = "compilation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Event> events;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;
}
