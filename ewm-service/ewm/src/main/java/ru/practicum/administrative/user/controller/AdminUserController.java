package ru.practicum.administrative.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.administrative.user.mapper.UserMapper;
import ru.practicum.administrative.user.service.AdminUserService;
import ru.practicum.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;


    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(defaultValue = "0", required = false) Long from,
                                @RequestParam(defaultValue = "10", required = false) Long size) {
        log.info("Get all admin users");
        return adminUserService.findAllByIdsAndFromAndSize(ids, from, size).stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("Create user: {}", userDto);
        return UserMapper.toUserDto(adminUserService.create(UserMapper.toUser(userDto)));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Delete user: {}", userId);
        adminUserService.deleteById(userId);
    }
}
