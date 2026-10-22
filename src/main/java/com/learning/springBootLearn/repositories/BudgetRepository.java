package com.learning.springBootLearn.repositories;

import com.learning.springBootLearn.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId")
    List<Budget> findBudgetsByUserId(@Param("userId") int userId);

    Optional<Budget> findByIdAndUserId(int id, int userId);
}
