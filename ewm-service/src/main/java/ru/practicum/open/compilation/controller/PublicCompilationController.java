package ru.practicum.open.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.open.compilation.mapper.CompilationMapper;
import ru.practicum.open.compilation.service.PublicCompilationService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    public PublicCompilationController(PublicCompilationService publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0", required = false) Long from,
                                       @RequestParam(defaultValue = "10", required = false) Long size) {
        return publicCompilationService.findAllPinedSortedFromAndSize(pinned, from, size).stream()
                .map(CompilationMapper::toCompilationDto)
                .toList();

    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable(value = "compId") Long id) {
        return CompilationMapper.toCompilationDto((publicCompilationService.findById(id)));
    }
}
