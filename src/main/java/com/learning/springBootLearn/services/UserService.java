package com.learning.springBootLearn.services;

import com.learning.springBootLearn.Mappers.UserMapper;
import com.learning.springBootLearn.dto.UserResponseDto;
import com.learning.springBootLearn.entities.User;
import com.learning.springBootLearn.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
    public UserResponseDto findUserByUsername(String username){
        User user= userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.fromEntityToDto(user);
    }
    public User findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }
}