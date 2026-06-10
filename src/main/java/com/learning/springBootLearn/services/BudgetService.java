package com.learning.springBootLearn.services;

import com.learning.springBootLearn.entities.Budget;
import com.learning.springBootLearn.repositories.BudgetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository ;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }
    public void createBudget(Budget budget){
        budgetRepository.save(budget);
    }
    public List<Budget> findAllBudgetsByUserId(int userId){
        return budgetRepository.findBudgetsByUserId(userId);
    }
    public Budget updateBudget(Budget budget) {
        Optional<Budget> outdatedBudget = budgetRepository.findById(budget.getId());
        
        if (outdatedBudget.isEmpty()) {
           throw new RuntimeException("Budget not found with id: " + budget.getId());
        }
        
        Budget updatedBudget = outdatedBudget.get();
        updatedBudget.setLimitAmount(budget.getLimitAmount());
        updatedBudget.setMonth(budget.getMonth());
        return budgetRepository.save(updatedBudget);
    }

    public void deleteBudget(int id) {
        Optional<Budget> outdatedBudget = budgetRepository.findById(id);

        if (outdatedBudget.isEmpty()) {
            throw new RuntimeException("Budget not found with id: " + id);
        }
        budgetRepository.deleteById(id);
    }
}
