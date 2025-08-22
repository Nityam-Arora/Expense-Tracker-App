package com.example.expensetrackerapp.Database.Expense;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.expensetrackerapp.Database.AppDatabase;
import com.example.expensetrackerapp.Database.Budget.BudgetDao;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Repository class for managing Expense data.
 * - Acts as a single source of truth for accessing expenses and budgets.
 * - Handles database operations in a background thread to avoid blocking the UI.
 */
public class ExpenseRepository {

    private ExpenseDao expenseDao;                   // DAO to access expense-related database operations
    private LiveData<List<ExpenseEntity>> allExpenses; // LiveData list of all expenses

    private BudgetDao budgetDao;                     // DAO to access budget-related database operations

    /**
     * Constructor: Initializes DAOs and LiveData.
     * @param application Application context for database instance
     */
    public ExpenseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application); // Get singleton database instance
        expenseDao = db.expenseDao();                          // Initialize Expense DAO
        budgetDao = db.budgetDao();                            // Initialize Budget DAO
        allExpenses = expenseDao.getAllExpenses();            // Fetch all expenses as LiveData
    }

    /**
     * Insert a new expense into the database.
     * Also subtracts the expense amount from the latest budget.
     * Runs on a background thread using Executors to prevent UI blocking.
     * @param expense ExpenseEntity to insert
     */
    public void insert(ExpenseEntity expense) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Insert new expense
            expenseDao.insertExpense(expense);

            // Subtract expense amount from the latest budget
            int latestBudgetId = budgetDao.getLatestBudgetId();
            budgetDao.subtractFromBudget(latestBudgetId, expense.getAmount());
        });
    }

    /**
     * Delete an expense from the database.
     * Runs on a background thread to avoid blocking the UI.
     * @param expense ExpenseEntity to delete
     */
    public void delete(ExpenseEntity expense) {
        Executors.newSingleThreadExecutor().execute(() -> expenseDao.deleteExpense(expense));
    }

    /** Returns LiveData of all expenses */
    public LiveData<List<ExpenseEntity>> getAllExpenses() {
        return allExpenses;
    }

    /** Returns LiveData of the last three expenses */
    public LiveData<List<ExpenseEntity>> getLastThreeExpenses() {
        return expenseDao.getLastThreeExpenses();
    }

    /** Returns LiveData of today's expenses */
    public LiveData<List<ExpenseEntity>> getTodayExpenses() {
        return expenseDao.getTodayExpenses();
    }

    /** Returns LiveData of this week's expenses */
    public LiveData<List<ExpenseEntity>> getWeeklyExpenses() {
        return expenseDao.getWeeklyExpenses();
    }

    /** Returns LiveData of this month's expenses */
    public LiveData<List<ExpenseEntity>> getMonthlyExpenses() {
        return expenseDao.getMonthlyExpenses();
    }

    /** Returns LiveData of total expenses grouped by category */
    public LiveData<List<ExpenseDao.CategoryTotal>> getCategoryTotal() {
        return expenseDao.getCategoryTotal();
    }
}
