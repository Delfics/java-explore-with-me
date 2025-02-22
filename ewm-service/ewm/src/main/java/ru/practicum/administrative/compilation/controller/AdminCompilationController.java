package ru.practicum.administrative.compilation.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.administrative.compilation.service.AdminCompilationService;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.mapper.CompilationMapper;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    AdminCompilationService adminCompilationService;

    public AdminCompilationController(AdminCompilationService adminCompilationService) {
        this.adminCompilationService = adminCompilationService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationDto create(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return CompilationMapper.toCompilationDto(adminCompilationService.createCompilation(newCompilationDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{compId}")
    public void delete(@PathVariable Long compId) {
        adminCompilationService.deleteById(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patch(@PathVariable Long compId,
                                @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        adminCompilationService.validateUpdateCompilationRequest(updateCompilationRequest);
        return CompilationMapper.toCompilationDto(adminCompilationService.patch(compId, updateCompilationRequest));
    }

}
