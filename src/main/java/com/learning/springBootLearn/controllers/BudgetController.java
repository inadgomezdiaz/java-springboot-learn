package com.learning.springBootLearn.controllers;

import com.learning.springBootLearn.dto.CreateBudgetDto;
import com.learning.springBootLearn.entities.Budget;
import com.learning.springBootLearn.entities.User;
import com.learning.springBootLearn.services.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/budget")
@RestController
public class BudgetController {

    final private BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping()
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody CreateBudgetDto budget, @AuthenticationPrincipal User user) {
        Budget budgetEntity = this.budgetService.createBudget(budget, user);
        return ResponseEntity.ok(budgetEntity);
    }

    @GetMapping()
    public ResponseEntity<List<Budget>> getBudgets(@AuthenticationPrincipal User user) {
        List<Budget> budgets = this.budgetService.getBudgetsByUser(user);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudget(@PathVariable int id, @AuthenticationPrincipal User user) {
        Budget budget = this.budgetService.getBudgetById(id, user);
        return ResponseEntity.ok(budget);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable int id, @Valid @RequestBody CreateBudgetDto budget, @AuthenticationPrincipal User user) {
        Budget updatedBudget = this.budgetService.updateBudget(id, budget, user);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable int id, @AuthenticationPrincipal User user) {
        this.budgetService.deleteBudget(id, user);
        return ResponseEntity.noContent().build();
    }
}
