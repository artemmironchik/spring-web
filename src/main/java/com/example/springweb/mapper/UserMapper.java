package com.example.springweb.mapper;

import com.example.springweb.dto.UserDto;
import com.example.springweb.entity.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserMapper {
    public static UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static User convertDtoToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
