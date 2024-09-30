package com.example.business.service;

import com.example.business.exceptions.FilteredUsersNotFoundException;
import com.example.domain.entity.User;
import com.example.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getFilteredUsers(String firstName, String lastName, String email) {
        List<User> usersByFilters = userRepository.findByFilters(firstName, lastName, email);
        if (usersByFilters.isEmpty()) {
            throw new FilteredUsersNotFoundException();
        }
        return usersByFilters;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User userDto, User existingUser) {
        User updatedUser = getUpdatedUser(userDto, existingUser);
        return userRepository.save(updatedUser);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User getUpdatedUser(User userDto, User existingUser) {
        if (userDto.getFirstName() != null) {
            existingUser.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            existingUser.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }
        return existingUser;
    }

    public List<User> findUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }
}

