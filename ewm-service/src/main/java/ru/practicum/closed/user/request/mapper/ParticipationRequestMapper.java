package ru.practicum.closed.user.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.closed.user.request.model.ParticipationRequest;
import ru.practicum.dto.ParticipationRequestDto;

@UtilityClass
public class ParticipationRequestMapper {
    public ParticipationRequestDto toParitcipationRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(participationRequest.getId());
        dto.setRequester(participationRequest.getRequester());
        dto.setEvent(participationRequest.getEvent());
        dto.setCreated(participationRequest.getCreated());
        dto.setStatus(participationRequest.getStatus());
        return dto;
    }

    public ParticipationRequest toParticipationRequest(ParticipationRequestDto participationRequestDto) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setId(participationRequestDto.getId());
        participationRequest.setRequester(participationRequestDto.getRequester());
        participationRequest.setEvent(participationRequestDto.getEvent());
        participationRequest.setCreated(participationRequestDto.getCreated());
        participationRequest.setStatus(participationRequestDto.getStatus());
        return participationRequest;
    }
}
