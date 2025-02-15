package ru.practicum.administrative.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.administrative.user.model.User;
import ru.practicum.administrative.user.repository.AdminUserStorage;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@Slf4j
public class AdminUserService {
    private final AdminUserStorage adminUserStorage;

    public AdminUserService(AdminUserStorage adminUserStorage) {
        this.adminUserStorage = adminUserStorage;
    }

    public List<User> findAllByIdsAndFromAndSize(List<Long> ids, Long from, Long size) {
        Pageable pageable = PageRequest.of(from.intValue(), size.intValue());
        if (ids != null && !ids.isEmpty()) {
            return adminUserStorage.findAllByIdsAndFromAndSize(ids, pageable);
        } else {
            return adminUserStorage.findAllByFromAndSize(pageable);
        }
    }

    public User create(User user) {
       if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            throw new BadRequestException("name must not be null or empty or blank");
        } else if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            throw new BadRequestException("email must not be null or empty or blank");
        }
        return adminUserStorage.save(user);
    }

    public void deleteById(Long id) {
        if (adminUserStorage.findById(id).isPresent()) {
            adminUserStorage.deleteById(id);
            log.debug("Deleted user with id {} successful", id);
        } else {
            throw new NotFoundException("User with id=" + id + " was not found");
        }
    }

    public User findById(Long id) {
        if (adminUserStorage.findById(id).isPresent()) {
            return adminUserStorage.findById(id).get();
        } else {
            throw new NotFoundException("User with id=" + id + " was not found");
        }
    }
}

