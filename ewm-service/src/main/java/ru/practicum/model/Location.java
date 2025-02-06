package ru.practicum.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Location {
    private float lat;
    private float lon;
}
