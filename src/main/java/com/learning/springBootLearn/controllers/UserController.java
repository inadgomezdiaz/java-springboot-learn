package com.learning.springBootLearn.controllers;

import com.learning.springBootLearn.dto.UserResponseDto;
import com.learning.springBootLearn.entities.User;
import com.learning.springBootLearn.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/users")
@RestController()
public class UserController {
    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> authenticatedUser(Principal principal) {

        UserResponseDto currentUser = userService.findUserByUsername(principal.getName());

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> allUsers(@PageableDefault(
            size = 10,
            sort = "email",
            direction = Sort.Direction.DESC
    ) Pageable pageable) {
        Page<UserResponseDto> users = userService.findAll(pageable);
        return ResponseEntity.ok(users.getContent());
    }
}

