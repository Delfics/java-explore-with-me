package ru.practicum.open.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.closed.user.event.mapper.EventMapper;
import ru.practicum.dto.CompilationDto;
import ru.practicum.open.compilation.model.Compilation;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(compilation.getEvents().stream()
                    .map(EventMapper::toEventShortDto)
                    .toList());
        } else {
            compilationDto.setEvents(List.of());
        }
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public Compilation toCompilation(CompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        if (compilationDto.getEvents() != null) {
            compilation.setEvents(compilationDto.getEvents().stream()
                    .map(EventMapper::toEvent)
                    .toList());
        } else {
            compilation.setEvents(List.of());
        }
        compilation.setId(compilationDto.getId());
        compilation.setPinned(compilationDto.getPinned());
        compilation.setTitle(compilationDto.getTitle());
        return compilation;
    }
}
