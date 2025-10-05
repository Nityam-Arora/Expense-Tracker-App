package com.example.expensetrackerapp.Database.Budget;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO (Data Access Object) for BudgetEntity.
 * Defines database operations related to budgets.
 */
@Dao
public interface BudgetDao {

    /**
     * Insert a new budget into the database.
     * @param budget BudgetEntity to insert
     */
    @Insert
    void insertBudget(BudgetEntity budget);

    /**
     * Retrieve the latest budget as LiveData.
     * Useful for observing changes in real-time in the UI.
     * @return LiveData containing the most recently added BudgetEntity
     */
    @Query("SELECT * FROM budget ORDER BY id DESC LIMIT 1")
    LiveData<BudgetEntity> getLatestBudgetLive();

    /**
     * Subtract a specific expense amount from the remaining budget.
     * This query updates the remainingAmount field of the latest budget.
     * @param budgetId ID of the budget to update
     * @param expense Expense amount to subtract
     */
    @Query("UPDATE budget SET remainingAmount = remainingAmount - :expense WHERE id = :budgetId")
    void subtractFromBudget(int budgetId, int expense);

    /**
     * Get the ID of the latest budget entry.
     * Useful when you need to reference the current budget for updates.
     * @return int representing the latest budget ID
     */
    @Query("SELECT * FROM budget ORDER BY id DESC LIMIT 1")
    int getLatestBudgetId();

    @Query("SELECT * FROM budget ORDER BY id DESC")
    LiveData<List<BudgetEntity>> getAllBudget();
}
