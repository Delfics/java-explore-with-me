package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.dto.ParticipationRequestDto;

@UtilityClass
public class ParticipationRequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(participationRequest.getId());
        dto.setRequester(participationRequest.getRequester());
        dto.setEvent(participationRequest.getEvent());
        dto.setCreated(participationRequest.getCreated());
        dto.setStatus(participationRequest.getStatus());
        return dto;
    }
}
