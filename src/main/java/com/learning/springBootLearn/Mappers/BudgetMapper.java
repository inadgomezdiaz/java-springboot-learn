package com.learning.springBootLearn.Mappers;

import com.learning.springBootLearn.dto.CreateBudgetDto;
import com.learning.springBootLearn.entities.Budget;
import com.learning.springBootLearn.entities.User;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    public static Budget fromDtoToEntity(CreateBudgetDto createBudgetDto, User user) {
        return new Budget(
            createBudgetDto.getLimitAmount(),
            createBudgetDto.getMonth(),
            user
        );
    }
}
