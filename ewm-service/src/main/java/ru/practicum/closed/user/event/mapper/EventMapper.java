package ru.practicum.closed.user.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.administrative.user.mapper.UserMapper;
import ru.practicum.closed.user.event.model.Event;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.open.category.mapper.CategoryMapper;

@UtilityClass
public class EventMapper {
    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setId(event.getId());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(eventFullDto.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public Event toEvent(EventFullDto eventFullDto) {
        Event event = new Event();
        event.setAnnotation(eventFullDto.getAnnotation());
        event.setCategory(CategoryMapper.toCategory(eventFullDto.getCategory()));
        event.setConfirmedRequests(eventFullDto.getConfirmedRequests());
        event.setCreatedOn(eventFullDto.getCreatedOn());
        event.setDescription(eventFullDto.getDescription());
        event.setEventDate(eventFullDto.getEventDate());
        event.setId(eventFullDto.getId());
        event.setInitiator(UserMapper.toUserShort(eventFullDto.getInitiator()));
        event.setLocation(eventFullDto.getLocation());
        event.setPaid(eventFullDto.getPaid());
        event.setParticipantLimit(eventFullDto.getParticipantLimit());
        event.setPublishedOn(eventFullDto.getPublishedOn());
        event.setRequestModeration(eventFullDto.getRequestModeration());
        event.setState(eventFullDto.getState());
        event.setTitle(eventFullDto.getTitle());
        event.setViews(eventFullDto.getViews());
        return event;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }
}
