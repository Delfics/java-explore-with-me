package ru.practicum.endpointhit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.endpointhit.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitStorageJpa extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(e)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri")
    List<ViewStatsDto> getStatsBetweenStartAndEnd(@Param("start") LocalDateTime start,
                                                  @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(e)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC ")
    List<ViewStatsDto> getStatsWithUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                     @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC ")
    List<ViewStatsDto> getStatsWithUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC")
    List<ViewStatsDto> getStatsWithUrisAndUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                                @Param("uris") List<String> uris);
}
