package com.learning.springBootLearn.services;

import com.learning.springBootLearn.Mappers.BudgetMapper;
import com.learning.springBootLearn.dto.CreateBudgetDto;
import com.learning.springBootLearn.entities.Budget;
import com.learning.springBootLearn.entities.User;
import com.learning.springBootLearn.exceptions.ResourceNotFoundException;
import com.learning.springBootLearn.repositories.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget createBudget(CreateBudgetDto budget, User user) {
        Budget budgetEntity = BudgetMapper.fromDtoToEntity(budget, user);
        return budgetRepository.save(budgetEntity);
    }

    public List<Budget> getBudgetsByUser(User user) {
        return budgetRepository.findBudgetsByUserId(user.getId());
    }

    public Budget getBudgetById(int id, User user) {
        return budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));
    }

    public Budget updateBudget(int id, CreateBudgetDto budget, User user) {
        Budget updatedBudget = getBudgetById(id, user);
        updatedBudget.setLimitAmount(budget.getLimitAmount());
        updatedBudget.setMonth(budget.getMonth());
        return budgetRepository.save(updatedBudget);
    }

    public void deleteBudget(int id, User user) {
        Budget budget = getBudgetById(id, user);
        budgetRepository.delete(budget);
    }
}
