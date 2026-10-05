package com.ilya.user_service.mapper.user;

import org.mapstruct.Mapper;
import com.ilya.user_service.dto.user.UserDto;
import com.ilya.user_service.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);
}
