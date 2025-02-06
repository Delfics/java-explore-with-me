package ru.practicum.closed.user.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.closed.user.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateRequestDto;

@UtilityClass
public class EvenRequestStatusMapper {
    public EventRequestStatusUpdateRequestDto toEventRequestStatusUpdateRequestDto
            (EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        EventRequestStatusUpdateRequestDto dto = new EventRequestStatusUpdateRequestDto();
        dto.setRequestIds(eventRequestStatusUpdateRequest.getRequestIds());
        dto.setStatus(eventRequestStatusUpdateRequest.getStatus());
        return dto;
    }

    public EventRequestStatusUpdateRequest toEventRequestStatusUpdateRequest
            (EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto) {
        EventRequestStatusUpdateRequest dto = new EventRequestStatusUpdateRequest();
        dto.setRequestIds(eventRequestStatusUpdateRequestDto.getRequestIds());
        dto.setStatus(eventRequestStatusUpdateRequestDto.getStatus());
        return dto;
    }
}
