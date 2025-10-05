package com.example.expensetrackerapp.Database.Expense;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel class for managing Expense data.
 * - Acts as a bridge between the UI and the repository.
 * - Provides LiveData for observing expense changes in real-time.
 */
public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository repository;                  // Repository to handle database operations
    private LiveData<List<ExpenseEntity>> allExpenses;     // LiveData holding all expenses

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);   // Initialize repository
        allExpenses = repository.getAllExpenses();        // Fetch all expenses initially
    }

    /**
     * Insert a new expense into the database
     * @param expense ExpenseEntity object to insert
     */
    public void insert(ExpenseEntity expense) {
        repository.insert(expense);
    }

    /**
     * Delete an expense from the database
     * @param expense ExpenseEntity object to delete
     */
    public void delete(ExpenseEntity expense) {
        repository.delete(expense);
    }

    /**
     * Get LiveData of all expenses
     * @return LiveData list of all ExpenseEntity
     */
    public LiveData<List<ExpenseEntity>> getAllExpenses() {
        return allExpenses;
    }

    /**
     * Get LiveData of the last three expenses
     * @return LiveData list of last three ExpenseEntity
     */
    public LiveData<List<ExpenseEntity>> getLastThreeExpenses() {
        return repository.getLastThreeExpenses();
    }

    /**
     * Get LiveData of today's expenses
     * @return LiveData list of today's ExpenseEntity
     */
    public LiveData<List<ExpenseEntity>> getTodayExpenses() {
        return repository.getTodayExpenses();
    }

    /**
     * Get LiveData of this week's expenses
     * @return LiveData list of weekly ExpenseEntity
     */
    public LiveData<List<ExpenseEntity>> getWeeklyExpenses(String startDate, String endDate) {
        return repository.getWeeklyExpenses(startDate, endDate);
    }

    /**
     * Get LiveData of this month's expenses
     * @return LiveData list of monthly ExpenseEntity
     */
    public LiveData<List<ExpenseEntity>> getMonthlyExpenses(String startDate, String endDate) {
        return repository.getMonthlyExpenses(startDate, endDate);
    }

    /**
     * Get LiveData of total expenses grouped by category
     * @return LiveData list of ExpenseDao.CategoryTotal
     */
    public LiveData<List<ExpenseDao.CategoryTotal>> getCategoryTotal(String startDate, String endDate) {
        return repository.getCategoryTotal(startDate, endDate);
    }
}
