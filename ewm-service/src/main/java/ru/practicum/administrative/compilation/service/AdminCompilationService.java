package ru.practicum.administrative.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.administrative.compilation.model.UpdateCompilationRequest;
import ru.practicum.closed.user.event.model.Event;
import ru.practicum.closed.user.event.repository.PrivateEventStorage;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.open.compilation.model.Compilation;
import ru.practicum.open.compilation.repository.PublicCompilationStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class AdminCompilationService {
    private final PublicCompilationStorage publicCompilationStorage;
    private final PrivateEventStorage eventStorage;

    public AdminCompilationService(PublicCompilationStorage publicCompilationStorage,
                                   PrivateEventStorage eventStorage) {
        this.publicCompilationStorage = publicCompilationStorage;
        this.eventStorage = eventStorage;
    }

    public Compilation createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        if (newCompilationDto.getEvents() != null) {
                Set<Long> eventSet = new HashSet<>(newCompilationDto.getEvents());
                if (eventSet.size() == newCompilationDto.getEvents().size()) {
                    compilation.setEvents(eventStorage.findEventsByEventIds(newCompilationDto.getEvents()));
                } else {
                    throw new ConflictException("Events have duplicate values");
                }
        } else {
            compilation.setEvents(List.of());
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        } else {
            compilation.setPinned(false);
        }
        compilation.setTitle(newCompilationDto.getTitle());
        return publicCompilationStorage.save(compilation);
    }

    public void deleteById(Long compId) {
        Optional<Compilation> byId = publicCompilationStorage.findById(compId);
        if (byId.isPresent()) {
            publicCompilationStorage.delete(byId.get());
        } else {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
    }

    @Transactional
    public Compilation patch(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Optional<Compilation> byId = publicCompilationStorage.findById(compId);
        if (byId.isPresent()) {
            if (updateCompilationRequest.getEvents() != null) {
                List<Event> events = eventStorage.findEventsByEventIds(updateCompilationRequest.getEvents());
                events.forEach(event -> event.setCompilation(byId.get())); // Устанавливаем связь с Compilation
                /*byId.get().setEvents(eventStorage.findEventsByEventIds(updateCompilationRequest.getEvents()));*/
                byId.get().setEvents(events);
            }
            if (updateCompilationRequest.getPinned() != null) {
                byId.get().setPinned(updateCompilationRequest.getPinned());
            }
            if (updateCompilationRequest.getTitle() != null) {
                byId.get().setTitle(updateCompilationRequest.getTitle());
            }
            return publicCompilationStorage.save(byId.get());
        } else {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
    }

    public void validateUpdateCompilationRequest(UpdateCompilationRequest updateCompilationRequest) {
        if (updateCompilationRequest.getTitle() != null) {
            if (updateCompilationRequest.getTitle().isBlank()) {
                throw new BadRequestException("Title не может быть пустым или состоять только из пробелов");
            }
            if (updateCompilationRequest.getTitle().length() < 1 || updateCompilationRequest.getTitle().length() > 50) {
                throw new BadRequestException("Field title должно содержать от 1 до 50 символов");
            }
            if (updateCompilationRequest.getTitle() != null && updateCompilationRequest.getTitle().trim().isEmpty()) {
                throw new BadRequestException("Title не может содержать только запятые");
            }
        }
    }
}
