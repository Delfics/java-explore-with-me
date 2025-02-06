package ru.practicum.administrative.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.administrative.user.model.User;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;

@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    public User toUserShort(UserShortDto UserShortDto) {
        User user = new User();
        user.setId(UserShortDto.getId());
        user.setName(UserShortDto.getName());
        return user;
    }

}
