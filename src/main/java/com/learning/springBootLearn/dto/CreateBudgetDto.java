package com.learning.springBootLearn.dto;
import com.learning.springBootLearn.enums.Month;

import java.math.BigDecimal;


public class CreateBudgetDto {
    public BigDecimal limitAmount;
    public Month month;

    public CreateBudgetDto(BigDecimal limitAmount, Month month) {
        this.limitAmount = limitAmount;
        this.month = month;
    }

    public Month month() {
        return month;
    }

    public void month(Month month) {
        this.month = month;
    }
    public BigDecimal limitAmount() {
        return limitAmount;
    }

    public void limitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

}
