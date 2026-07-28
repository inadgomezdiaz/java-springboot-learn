package com.learning.springBootLearn.controllers;

import com.learning.springBootLearn.dto.CreateBudgetDto;
import com.learning.springBootLearn.entities.Budget;
import com.learning.springBootLearn.entities.User;
import com.learning.springBootLearn.services.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/budget")
@RestController
public class BudgetController {

    final private BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }
@PostMapping()
    public ResponseEntity<Budget> createBudget(@RequestBody CreateBudgetDto budget, @AuthenticationPrincipal User user) {
        Budget budgetEntity = this.budgetService.createBudget(budget,user);
        return  ResponseEntity.ok(budgetEntity);
    }
}
