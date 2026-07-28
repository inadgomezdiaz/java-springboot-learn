package com.learning.springBootLearn.entities;

import com.learning.springBootLearn.enums.Month;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Table(name="Budget")
@Entity
public class Budget{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private BigDecimal limitAmount;

    @Column(nullable = false)
    private Month month;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Budget(BigDecimal limitAmount, Month month, User user) {
        this.limitAmount = limitAmount;
        this.month = month;
        this.user = user;

    }

    public User getUser() {
        return user;
    }


    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public Month getMonth() {
        return month;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
