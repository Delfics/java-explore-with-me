package ru.practicum.administrative.compilation.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    @PostMapping
    public CompilationDto create(@RequestBody CompilationDto compilationDto) {
        return compilationDto;
    }

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable Long compId) {
        return;
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable Long compId, @RequestBody CompilationDto compilationDto) {
        return null;
    }

}
