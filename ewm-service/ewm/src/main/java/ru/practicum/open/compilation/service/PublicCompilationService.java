package ru.practicum.open.compilation.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.open.compilation.repository.PublicCompilationStorage;

import java.util.List;

@Service
public class PublicCompilationService {
    private final PublicCompilationStorage publicCompilationStorage;

    PublicCompilationService(PublicCompilationStorage publicCompilationStorage) {
        this.publicCompilationStorage = publicCompilationStorage;
    }

    @Transactional
    public List<Compilation> findAllPinedSortedFromAndSize(Boolean pined, Long from, Long size) {
        Pageable pageable = PageRequest.of(from.intValue(), size.intValue());
        if (pined != null && pined) {
            return publicCompilationStorage.findAllPinedSortedFromAndSize(true, pageable);
        } else {
            return publicCompilationStorage.findAllSortedFromAndSize(pageable);
        }
    }

    @Transactional
    public Compilation findById(Long id) {
        Compilation compilation = publicCompilationStorage.findById(id).orElse(null);
        if (compilation == null) {
            throw new NotFoundException("Compilation with id=" + id + " was not found");
        }
        return compilation;
    }
}
