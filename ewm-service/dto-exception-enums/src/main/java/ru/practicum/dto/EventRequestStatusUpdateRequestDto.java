package ru.practicum.dto;

import lombok.Data;
import ru.practicum.enums.Status;

import java.util.ArrayList;

@Data
public class EventRequestStatusUpdateRequestDto {

    private ArrayList<Long> requestIds;

    private Status status;
}
