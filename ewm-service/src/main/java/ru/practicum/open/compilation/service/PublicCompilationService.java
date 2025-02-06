package ru.practicum.open.compilation.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.open.compilation.model.Compilation;
import ru.practicum.open.compilation.repository.PublicCompilationStorage;

import java.util.List;

@Service
public class PublicCompilationService {
    private final PublicCompilationStorage publicCompilationStorage;

    PublicCompilationService(PublicCompilationStorage publicCompilationStorage) {
        this.publicCompilationStorage = publicCompilationStorage;
    }

    public List<Compilation> findAllPinedSortedFromAndSize(Boolean pined, Long from, Long size) {
        Pageable pageable = PageRequest.of(from.intValue(), size.intValue());
        if (pined != null && pined) {
            return publicCompilationStorage.findAllPinedSortedFromAndSize(pined, pageable);
        } else {
            return publicCompilationStorage.findAllSortedFromAndSize(pageable);
        }
    }

    public Compilation findById(Long id) {
        Compilation compilation = publicCompilationStorage.findById(id).orElse(null);
        if (compilation == null) {
            throw new NotFoundException("Compilation with id=" + id + " was not found");
        }
        return compilation;
    }
}
