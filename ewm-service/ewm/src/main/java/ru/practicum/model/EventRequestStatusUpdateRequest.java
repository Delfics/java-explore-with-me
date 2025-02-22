package ru.practicum.model;

import lombok.Data;
import ru.practicum.enums.Status;

import java.util.ArrayList;

@Data
public class EventRequestStatusUpdateRequest {

    ArrayList<Long> requestIds;

    Status status;
}
