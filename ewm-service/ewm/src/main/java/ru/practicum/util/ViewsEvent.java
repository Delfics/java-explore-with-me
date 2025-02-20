package ru.practicum.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.HttpClientStats;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ViewsEvent {
    @Value("${url}")
    private String url;

    public EndpointHitDto createRequestEndpointHitDto(String app, String requestUri, String remoteAddr) throws Exception {
        log.info("String - app={} requestUri={} remoteAddr={}", app, requestUri, remoteAddr);
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(app);
        endpointHitDto.setUri(requestUri);
        endpointHitDto.setIp(remoteAddr);
        endpointHitDto.setTimestamp(LocalDateTime.now());
        HttpClientStats httpClientStats = new HttpClientStats(url);
        httpClientStats.sendCreateEndpointHit(endpointHitDto);
        return endpointHitDto;
    }

    public Long getViewsWithUniqueForEventByEventIdAndCreatedOn(Long eventId, Long viewEvents, LocalDateTime createdOn,
                                                                String uri, Boolean unique) throws Exception {
        Long zero = 0L;
        String uriEvent = "/events/" + eventId;
        HttpClientStats httpClientStats = new HttpClientStats(url);
        List<ViewStatsDto> viewStatsDtos = httpClientStats.sendGetViewStatsWithUnique(createdOn, LocalDateTime.now(),
                unique);
        if (!viewStatsDtos.isEmpty()) {
            ViewStatsDto viewStatsDto = viewStatsDtos.get(0);
            if (viewStatsDto.getUri().equals(uriEvent)) {
                if (!Objects.equals(viewStatsDto.getHits(), viewEvents) && viewEvents.equals(zero)) {
                    return viewStatsDto.getHits();
                }
                if (viewEvents > viewStatsDto.getHits()) {
                    viewEvents = viewEvents - viewStatsDto.getHits();
                    return viewEvents;
                }
                if (viewEvents.equals(viewStatsDto.getHits())) {
                    return viewStatsDto.getHits();
                }
                return zero;
            } else {
                return zero;
            }
        } else {
            return zero;
        }
    }
}

