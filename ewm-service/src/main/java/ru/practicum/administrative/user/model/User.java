package ru.practicum.administrative.user.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class User {
    @Column(name = "email")
    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
