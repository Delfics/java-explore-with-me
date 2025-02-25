package ru.practicum.administrative.event.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.util.Utils;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.model.Event;
import ru.practicum.closed.user.event.repository.PrivateEventStorage;
import ru.practicum.closed.user.event.service.PrivateUserEventService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.open.category.service.PublicCategoryService;
import ru.practicum.enums.State;
import ru.practicum.enums.StateAction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminEventService {
    private final PublicCategoryService publicCategoryService;
    private final PrivateUserEventService privateUserEventService;
    private final PrivateEventStorage privateEventStorage;
    private final EntityManager entityManager;

    public AdminEventService(PrivateUserEventService privateUserEventService,
                             PrivateEventStorage privateEventStorage, PublicCategoryService publicCategoryService,
                             EntityManager entityManager) {
        this.privateUserEventService = privateUserEventService;
        this.privateEventStorage = privateEventStorage;
        this.publicCategoryService = publicCategoryService;
        this.entityManager = entityManager;
    }

    public List<Event> findAll(List<Long> users, List<String> states, List<Long> categories,
                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        DateTimeFormatter formatter = Utils.dateTimeFormatter;
        LocalDateTime rangeStartParsed = null;
        LocalDateTime rangeEndParsed = null;
        boolean hasCondition = false;

        if (rangeStart != null && !rangeStart.isEmpty()) {
            rangeStartParsed = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            rangeEndParsed = LocalDateTime.parse(rangeEnd, formatter);
        }

        StringBuilder query = new StringBuilder("SELECT e FROM Event e ");

        if (users != null && !users.isEmpty()) {
            query.append(" WHERE").append(" e.initiator.id IN :users");
            hasCondition = true;
        }

        if (states != null && !states.isEmpty()) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.state IN :states");
            hasCondition = true;
        }

        if (categories != null && !categories.isEmpty()) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.category.id IN :categories");
            hasCondition = true;
        }

        if (rangeStartParsed != null) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.eventDate >= :rangeStart");
            hasCondition = true;
        }
        if (rangeEndParsed != null) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.eventDate <= :rangeEnd");
        }

        TypedQuery<Event> typedQuery = entityManager.createQuery(query.toString(), Event.class);

        if (users != null && !users.isEmpty()) {
            typedQuery.setParameter("users", users);
        }
        if (states != null && !states.isEmpty()) {
            typedQuery.setParameter("states", states.stream()
                    .map(State::valueOf) // Преобразуем строки в enum
                    .collect(Collectors.toList()));
        }
        if (categories != null && !categories.isEmpty()) {
            typedQuery.setParameter("categories", categories);
        }
        if (rangeStartParsed != null) {
            typedQuery.setParameter("rangeStart", rangeStartParsed);
        }
        if (rangeEndParsed != null) {
            typedQuery.setParameter("rangeEnd", rangeEndParsed);
        }

        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    public Event updateEventAdminRequest(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event byEventId = privateUserEventService.findByEventId(eventId);

        validateEventDate(byEventId, updateEventAdminRequest.getEventDate());

        updateEventState(byEventId, updateEventAdminRequest.getStateAction());

        updateEventFields(byEventId, updateEventAdminRequest);

        byEventId.setPublishedOn(LocalDateTime.now());

        return privateEventStorage.save(byEventId);
    }

    private void validateEventDate(Event event, LocalDateTime newEventDate) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Field: eventDate. Должно быть хотя бы за 1 час до публикации.");
        }
        if (newEventDate != null && newEventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Field: eventDate. При обновлении дата должна быть минимум за 1 час.");
        }
    }

    private void updateEventState(Event event, StateAction stateAction) {
        if (stateAction == null) return;

        if (event.getState().equals(State.PENDING) && stateAction.equals(StateAction.PUBLISH_EVENT)) {
            event.setState(State.PUBLISHED);
        } else if ((event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING)) &&
                stateAction.equals(StateAction.REJECT_EVENT)) {
            event.setState(State.CANCELED);
        } else {
            throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
        }
    }

    private void updateEventFields(Event event, UpdateEventAdminRequest request) {
        if (request.getAnnotation() != null) event.setAnnotation(request.getAnnotation());
        if (request.getCategory() != null) event.setCategory(publicCategoryService.findById(request.getCategory()));
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventDate() != null) event.setEventDate(request.getEventDate());
        if (request.getLocation() != null) event.setLocation(request.getLocation());
        if (request.getPaid() != null) event.setPaid(request.getPaid());
        if (request.getParticipantLimit() != null) event.setParticipantLimit(request.getParticipantLimit());
        if (request.getRequestModeration() != null) event.setRequestModeration(request.getRequestModeration());
        if (request.getTitle() != null) event.setTitle(request.getTitle());
    }

    public void validateUpdateEventAdminRequest(UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getAnnotation() != null) {
            if (updateEventAdminRequest.getAnnotation().isBlank()) {
                throw new BadRequestException("Field annotation не может быть пустым или состоять только из пробелов");
            }
            if (updateEventAdminRequest.getAnnotation().length() < 20 || updateEventAdminRequest.getAnnotation().length() > 2000) {
                throw new BadRequestException("Field annotation должно содержать от 20 до 2000 символов");
            }
            if (updateEventAdminRequest.getAnnotation().trim().isEmpty()) {
                throw new BadRequestException("Field annotation не может содержать только запятые");
            }
        }

        if (updateEventAdminRequest.getDescription() != null) {
            if (updateEventAdminRequest.getDescription().isBlank()) {
                throw new BadRequestException("Field description не может быть пустым или состоять только из пробелов");
            }
            if (updateEventAdminRequest.getDescription().length() < 20 || updateEventAdminRequest.getDescription().length() > 7000) {
                throw new BadRequestException("Field description должно содержать от 20 до 7000 символов");
            }
            if (updateEventAdminRequest.getDescription().trim().isEmpty()) {
                throw new BadRequestException("Field description не может состоять только из пробельных символов");
            }
        }

        if (updateEventAdminRequest.getTitle() != null) {
            if (updateEventAdminRequest.getTitle().isBlank()) {
                throw new BadRequestException("Field title не может быть пустым или состоять только из пробелов");
            }
            if (updateEventAdminRequest.getTitle().length() < 3 || updateEventAdminRequest.getTitle().length() > 120) {
                throw new BadRequestException("Field title должно содержать от 3 до 120 символов");
            }
            if (updateEventAdminRequest.getTitle().trim().isEmpty()) {
                throw new BadRequestException("Field title не может содержать только запятые");
            }
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Field eventDate не может быть в прошлом");
            }
        }

        if (updateEventAdminRequest.getParticipantLimit() != null && updateEventAdminRequest.getParticipantLimit() < 0) {
            throw new BadRequestException("Field participantLimit не может быть отрицательным");
        }
    }
}
