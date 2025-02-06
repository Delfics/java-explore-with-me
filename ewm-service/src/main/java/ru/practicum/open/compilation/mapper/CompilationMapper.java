package ru.practicum.open.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CompilationDto;
import ru.practicum.open.compilation.model.Compilation;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setEvents(compilation.getEvents());
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public Compilation toCompilation(CompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setEvents(compilationDto.getEvents());
        compilation.setId(compilationDto.getId());
        compilation.setPinned(compilationDto.getPinned());
        compilation.setTitle(compilationDto.getTitle());
        return compilation;
    }
}
