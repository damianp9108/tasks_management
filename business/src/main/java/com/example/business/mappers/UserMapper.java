package com.example.business.mappers;

import com.example.business.dto.UserDTO;
import com.example.domain.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User mapToUser(UserDTO userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .joiningDate(LocalDate.now())
                .build();
    }

    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public List<UserDTO> mapToUserDTOs(List<User> users) {
        return users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }
}
