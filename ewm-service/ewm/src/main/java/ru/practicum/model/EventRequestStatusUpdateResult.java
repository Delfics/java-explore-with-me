package ru.practicum.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EventRequestStatusUpdateResult {

    List<ParticipationRequest> confirmedRequests = new ArrayList<>();

    List<ParticipationRequest> rejectedRequests = new ArrayList<>();
}
