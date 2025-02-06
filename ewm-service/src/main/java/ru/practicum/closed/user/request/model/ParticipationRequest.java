package ru.practicum.closed.user.request.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.status.Status;

import java.time.LocalDateTime;

@Data
@Entity
public class ParticipationRequest {
    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "event_id")
    private Long event;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "requester_id")
    private Long requester;

    @Column(name = "status")
    private Status status;
}
