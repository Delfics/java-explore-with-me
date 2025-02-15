package ru.practicum.administrative.event.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.administrative.util.Utils;
import ru.practicum.administrative.event.model.UpdateEventAdminRequest;
import ru.practicum.closed.user.event.model.Event;
import ru.practicum.closed.user.event.repository.PrivateEventStorage;
import ru.practicum.closed.user.event.service.PrivateUserEventService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.open.category.service.PublicCategoryService;
import ru.practicum.state.State;
import ru.practicum.state_action.StateAction;

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
        DateTimeFormatter formatter = Utils.DATE_TIME_FORMATTER;
        LocalDateTime rangeStartParsed = null;
        LocalDateTime rangeEndParsed = null;
        boolean hasCondition = false;

        // Парсим даты диапазона, если они есть
        if (rangeStart != null && !rangeStart.isEmpty()) {
            rangeStartParsed = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            rangeEndParsed = LocalDateTime.parse(rangeEnd, formatter);
        }

        StringBuilder query = new StringBuilder("SELECT e FROM Event e ");

        if (users != null && !users.isEmpty()) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.initiator.id IN :users");
            hasCondition = true;
        }

// Фильтрация по состоянию
        if (states != null && !states.isEmpty()) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.state IN :states");
            hasCondition = true;
        }

// Фильтрация по категориям
        if (categories != null && !categories.isEmpty()) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.category.id IN :categories");
            hasCondition = true;
        }

// Фильтрация по временным диапазонам
        if (rangeStartParsed != null) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.eventDate >= :rangeStart");
            hasCondition = true;
        }
        if (rangeEndParsed != null) {
            query.append(hasCondition ? " AND" : " WHERE").append(" e.eventDate <= :rangeEnd");
        }

        // Создаем запрос и параметры
        TypedQuery<Event> typedQuery = entityManager.createQuery(query.toString(), Event.class);

        // Устанавливаем параметры запроса
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

        // Пагинация
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        // Выполнение запроса и получение результатов
        List<Event> events = typedQuery.getResultList();

        return events;
    }

    public Event updateEventAdminRequest(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event byEventId = privateUserEventService.findByEventId(eventId);
        if (byEventId.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException(("Field: eventDate. Error: должно содержать дату, которая еще не наступила " +
                    "или за 1 час от публикации"));
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException("Field: eventDate. при обновлении должно содержать дату," +
                        " которая еще не наступила или за 1 час от публикации");
            }
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (byEventId.getState().equals(State.PENDING) &&
                    updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                byEventId.setState(State.PUBLISHED);
            } else if ((byEventId.getState().equals(State.CANCELED) || byEventId.getState().equals(State.PENDING)) &&
                    updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
                byEventId.setState(State.CANCELED);
            } else {
                throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
            }
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            byEventId.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            byEventId.setCategory(publicCategoryService.findById(updateEventAdminRequest.getCategory()));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            byEventId.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            byEventId.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            byEventId.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            byEventId.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            byEventId.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        byEventId.setPublishedOn(LocalDateTime.now());
        if (updateEventAdminRequest.getRequestModeration() != null) {
            byEventId.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            byEventId.setTitle(updateEventAdminRequest.getTitle());
        }
        return privateEventStorage.save(byEventId);
    }

    public void validateUpdateEventAdminRequest(UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getAnnotation() != null) {
            if (updateEventAdminRequest.getAnnotation().isBlank()) {
                throw new BadRequestException("Field annotation не может быть пустым или состоять только из пробелов");
            }
            if (updateEventAdminRequest.getAnnotation().length() < 20 || updateEventAdminRequest.getAnnotation().length() > 2000) {
                throw new BadRequestException("Field annotation должно содержать от 20 до 2000 символов");
            }
            if (updateEventAdminRequest.getAnnotation() != null && updateEventAdminRequest.getAnnotation().trim().isEmpty()) {
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
            if (updateEventAdminRequest.getDescription() != null && updateEventAdminRequest.getDescription().trim().isEmpty()) {
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
            if (updateEventAdminRequest.getTitle() != null && updateEventAdminRequest.getTitle().trim().isEmpty()) {
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
