package com.example.expensetrackerapp.Database.Expense;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insertExpense(ExpenseEntity expense);

    @Delete
    void deleteExpense(ExpenseEntity expense);

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<ExpenseEntity>> getAllExpenses();

    @Query("SELECT * FROM expenses ORDER BY id DESC LIMIT 3")
    LiveData<List<ExpenseEntity>> getLastThreeExpenses();

    @Query("SELECT * FROM expenses ORDER BY id ASC")
    List<ExpenseEntity> getAllExpensesList(); // For internal use, not LiveData

    @Query("SELECT * FROM expenses WHERE date >= DATE('now', '-6 days') ORDER BY date DESC, time DESC")
    LiveData<List<ExpenseEntity>> getLastWeekExpenses();
}
