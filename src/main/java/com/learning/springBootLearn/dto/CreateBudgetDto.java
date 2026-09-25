package com.learning.springBootLearn.dto;

import com.learning.springBootLearn.enums.Month;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateBudgetDto {

    @NotNull
    @Positive
    private BigDecimal limitAmount;

    @NotNull
    private Month month;

    public CreateBudgetDto() {
    }

    public CreateBudgetDto(BigDecimal limitAmount, Month month) {
        this.limitAmount = limitAmount;
        this.month = month;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }
}
