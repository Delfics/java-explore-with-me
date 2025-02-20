package ru.practicum.open.event.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.util.Utils;
import ru.practicum.closed.user.event.model.Event;
import ru.practicum.closed.user.event.repository.PrivateEventStorage;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.open.category.repository.PublicCategoryStorage;
import ru.practicum.enums.State;
import ru.practicum.util.ViewsEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Slf4j
public class PublicEventService {
    private final String app = "ewm-main-service";
    private final PrivateEventStorage privateEventStorage;
    private final EntityManager entityManager;
    private final PublicCategoryStorage publicCategoryStorage;
    private final ViewsEvent viewsEvent;

    public PublicEventService(PrivateEventStorage privateEventStorage, EntityManager entityManager,
                              PublicCategoryStorage publicCategoryStorage, ViewsEvent viewsEvent) {
        this.privateEventStorage = privateEventStorage;
        this.entityManager = entityManager;
        this.publicCategoryStorage = publicCategoryStorage;
        this.viewsEvent = viewsEvent;
    }

    public List<Event> findAllEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                                     HttpServletRequest request) throws Exception {
        DateTimeFormatter formatter = Utils.dateTimeFormatter;
        LocalDateTime rangeStartParsed = null;
        LocalDateTime rangeEndParsed = null;

        if (rangeStart != null && !rangeStart.isEmpty()) {
            rangeStartParsed = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            rangeEndParsed = LocalDateTime.parse(rangeEnd, formatter);
        }

        if (rangeStartParsed == null) {
            rangeStartParsed = LocalDateTime.now();
        }

        StringBuilder query = new StringBuilder("SELECT e FROM Event e WHERE e.publishedOn IS NOT NULL");

        if (text != null && !text.isEmpty()) {
            query.append(" AND (LOWER(e.annotation) LIKE LOWER(:text) OR LOWER(e.description) LIKE LOWER(:text))");
        }
        if (categories != null && !categories.isEmpty()) {
            if (publicCategoryStorage.existsListCategoriesId(categories)) {
                query.append(" AND e.category.id IN :categories");
            } else {
                throw new BadRequestException("In request Categories are not exist");
            }
        }
        if (paid != null) {
            query.append(" AND e.paid = :paid");
        }
        query.append(" AND e.eventDate >= :rangeStart");
        if (rangeEndParsed != null) {
            query.append(" AND e.eventDate <= :rangeEnd");
        }
        if (onlyAvailable != null && onlyAvailable) {
            query.append(" AND e.participantLimit > e.confirmedRequests");
        }

        if (sort != null && !sort.isEmpty()) {
            if (sort.equals("eventDate")) {
                query.append(" ORDER BY e.eventDate");
            } else if (sort.equals("views")) {
                query.append(" ORDER BY e.views");
            }
        }

        TypedQuery<Event> typedQuery = entityManager.createQuery(query.toString(), Event.class);

        if (text != null && !text.isEmpty()) {
            typedQuery.setParameter("text", "%" + text + "%");
        }
        if (categories != null && !categories.isEmpty()) {
            typedQuery.setParameter("categories", categories);
        }
        if (paid != null) {
            typedQuery.setParameter("paid", paid);
        }
        typedQuery.setParameter("rangeStart", rangeStartParsed);
        if (rangeEndParsed != null) {
            typedQuery.setParameter("rangeEnd", rangeEndParsed);
        }

        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        viewsEvent.createRequestEndpointHitDto(app, request.getRequestURI(),
                request.getRemoteAddr());

        typedQuery.getResultList()
                .forEach(e -> {
                    if (e.getState() != State.PUBLISHED) {
                        throw new NotFoundException("Event must be published");
                    }
                });
        for (Event event : typedQuery.getResultList()) {
            Long views = event.getViews();
            views = views + 1;
            event.setViews(views);
            privateEventStorage.save(event);
        }

        return typedQuery.getResultList();
    }

    public Event getEventById(Long eventId, HttpServletRequest request) throws Exception {
        Event byEventId = privateEventStorage.findByEventId(eventId);
        boolean unique = true;
        if (byEventId == null || byEventId.getState() != State.PUBLISHED) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        } else {
            EndpointHitDto endpointHitDto = viewsEvent.createRequestEndpointHitDto(app, request.getRequestURI(),
                    request.getRemoteAddr());

            Long views = viewsEvent.getViewsWithUniqueForEventByEventIdAndCreatedOn(byEventId.getId(),
                    byEventId.getViews(), byEventId.getCreatedOn(), endpointHitDto.getUri(), unique);

            byEventId.setViews(views);
            return privateEventStorage.save(byEventId);
        }
    }
}
