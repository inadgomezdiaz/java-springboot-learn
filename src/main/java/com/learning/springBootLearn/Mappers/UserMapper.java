package com.learning.springBootLearn.Mappers;

import com.learning.springBootLearn.dto.UserResponseDto;
import com.learning.springBootLearn.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDto fromEntityToDto(User user) {
        return new UserResponseDto(
            user.getEmail(),
            user.getFullName()
        );
    }
}
