package ru.practicum.endpointhit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.endpointhit.model.EndpointHit;
import ru.practicum.endpointhit.repository.EndpointHitStorageJpa;

@Slf4j
@Service
public class EndpointHitService {
    private final EndpointHitStorageJpa endpointHitStorageJpa;

    public EndpointHitService(EndpointHitStorageJpa endpointHitStorageJpa) {
        this.endpointHitStorageJpa = endpointHitStorageJpa;
    }

    public EndpointHit create(EndpointHit endpointHit) {
        return endpointHitStorageJpa.save(endpointHit);
    }
}
