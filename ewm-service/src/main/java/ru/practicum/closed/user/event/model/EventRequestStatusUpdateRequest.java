package ru.practicum.closed.user.event.model;

import lombok.Data;
import ru.practicum.status.Status;

import java.util.ArrayList;

@Data
public class EventRequestStatusUpdateRequest {
    ArrayList<Long> requestIds;
    Status status;
}
