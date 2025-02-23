package ru.practicum.open.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.HttpClientStats;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ViewsEvent {
    private final HttpClientStats clientStats;

    @Autowired
    public ViewsEvent(HttpClientStats clientStats) {
        this.clientStats = clientStats;
    }

    public EndpointHitDto createRequestEndpointHitDto(String app, String requestUri, String remoteAddr) throws Exception {
        log.info("String - app={} requestUri={} remoteAddr={}", app, requestUri, remoteAddr);
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(app);
        endpointHitDto.setUri(requestUri);
        endpointHitDto.setIp(remoteAddr);
        endpointHitDto.setTimestamp(LocalDateTime.now());
        clientStats.sendCreateEndpointHit(endpointHitDto);
        return endpointHitDto;
    }

    public Long getViewsWithUniqueForEventByEventIdAndCreatedOn(Long eventId, Long viewEvents, LocalDateTime createdOn,
                                                                String uri, Boolean unique) throws Exception {
        Long zero = 0L;
        String uriEvent = "/events/" + eventId;
        List<ViewStatsDto> viewStatsDtos = clientStats.sendGetViewStatsWithUnique(createdOn, LocalDateTime.now(),
                unique);
        if (!viewStatsDtos.isEmpty()) {
            ViewStatsDto viewStatsDto = viewStatsDtos.getFirst();
            if (viewStatsDto.getUri().equals(uriEvent)) {
                return viewStatsDto.getHits();
            } else {
                return zero;
            }
        } else {
            return zero;
        }
    }
}

