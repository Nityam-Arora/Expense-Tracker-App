package com.example.expensetrackerapp.Database.Expense;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO (Data Access Object) for ExpenseEntity.
 * Defines database operations for expenses.
 * All methods run asynchronously when observed via LiveData.
 */
@Dao
public interface ExpenseDao {

    /**
     * Class to hold the total amount for each category.
     * Used for grouping expenses by category.
     */
    class CategoryTotal {
        public String category;  // Expense category name
        public double total;     // Total amount spent in this category
    }

    /** Insert a new expense into the database */
    @Insert
    void insertExpense(ExpenseEntity expense);

    /** Delete an existing expense from the database */
    @Delete
    void deleteExpense(ExpenseEntity expense);

    /**
     * Retrieve all expenses, ordered by date descending (latest first)
     * @return LiveData list of ExpenseEntity
     */
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<ExpenseEntity>> getAllExpenses();

    /**
     * Retrieve the last three expenses added
     * @return LiveData list of the 3 most recent ExpenseEntity
     */
    @Query("SELECT * FROM expenses ORDER BY date DESC, time DESC LIMIT 3")
    LiveData<List<ExpenseEntity>> getLastThreeExpenses();

    /**
     * Retrieve today's expenses
     * Filters by current local date
     * @return LiveData list of today's ExpenseEntity
     */
    @Query("SELECT * FROM expenses WHERE date = DATE('now', '0 days', 'localtime') ORDER BY time DESC")
    LiveData<List<ExpenseEntity>> getTodayExpenses();

    /**
     * Retrieve this week's expenses
     * Filters expenses from the last 7 days including today
     * @return LiveData list of weekly ExpenseEntity
     */
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, time DESC")
    LiveData<List<ExpenseEntity>> getWeeklyExpenses(String startDate, String endDate);

    /**
     * Retrieve this month's expenses
     * Filters expenses from the last 30 days including today
     * @return LiveData list of monthly ExpenseEntity
     */

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, time DESC")
    LiveData<List<ExpenseEntity>> getMonthlyExpenses(String startDate, String endDate);
    /**
     * Retrieve total expense amount grouped by category
     * Considers the total amounts based on Start Date and End Date
     * @return LiveData list of CategoryTotal objects
     */
    @Query("SELECT category, SUM(amount) AS total FROM expenses WHERE date BETWEEN :startDate AND :endDate GROUP BY category ORDER BY total DESC")
    LiveData<List<CategoryTotal>> getCategoryTotal(String startDate, String endDate);
}
