package ru.practicum.dto;

import lombok.Data;
import ru.practicum.status.Status;

import java.util.ArrayList;

@Data
public class EventRequestStatusUpdateRequestDto {
    ArrayList<Long> requestIds;
    Status status;
}
