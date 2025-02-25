package ru.practicum.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    private List<Long> events;

    private Long id;

    private Boolean pinned;

    private String title;
}
