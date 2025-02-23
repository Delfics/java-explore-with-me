package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.Location;
import ru.practicum.enums.State;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Event {

    @Column(name = "annotation", length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description", length = 7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Embedded
    private Location location;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "states")
    private State state;

    @Column(name = "title", length = 120)
    private String title;
    private Long views;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id", referencedColumnName = "id")
    private Compilation compilation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}