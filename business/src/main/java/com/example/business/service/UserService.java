package com.example.business.service;

import com.example.business.dto.UserDto;
import com.example.business.exceptions.EmailAlreadyExistsException;
import com.example.business.exceptions.FilteredUsersNotFoundException;
import com.example.business.exceptions.MissingEmailException;
import com.example.business.exceptions.UserNotFoundException;
import com.example.business.mappers.UserMapper;
import com.example.domain.entity.User;
import com.example.domain.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<User> getFilteredUsers(String firstName, String lastName, String email) {
        List<User> usersByFilters = userRepository.findByFilters(firstName, lastName, email);
        if (usersByFilters.isEmpty()) {
            throw new FilteredUsersNotFoundException();
        }
        return usersByFilters;
    }

    public UserDto getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.mapToUserDto(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserDto addUser(UserDto userDto) {
        if (StringUtils.isEmpty(userDto.getEmail())) {
            throw new MissingEmailException();
        }
        checkDoesEmailExistInDB(userDto.getEmail());
        userRepository.save(userMapper.mapToUser(userDto));
        return userDto;
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = findUserById(id);
        User updatedUser = getUpdatedUser(userDto, existingUser);
        userRepository.save(updatedUser);
        return userDto;
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User getUpdatedUser(UserDto userDto, User existingUser) {
        if (userDto.getEmail() != null && !userDto.getEmail().equals(existingUser.getEmail())) {
            checkDoesEmailExistInDB(userDto.getEmail());
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getFirstName() != null) {
            existingUser.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            existingUser.setLastName(userDto.getLastName());
        }
        return existingUser;
    }

    public List<User> findUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    private void checkDoesEmailExistInDB(String email) {
        userRepository.findByEmail(email)
                .ifPresent(existingUser -> {
                    throw new EmailAlreadyExistsException(email);
                });
    }
}

