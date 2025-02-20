package ru.practicum.closed.user.event.model;

import lombok.Data;
import ru.practicum.closed.user.request.model.ParticipationRequest;

import java.util.ArrayList;
import java.util.List;

@Data
public class EventRequestStatusUpdateResult {

    List<ParticipationRequest> confirmedRequests = new ArrayList<>();

    List<ParticipationRequest> rejectedRequests = new ArrayList<>();
}
