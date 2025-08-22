package com.example.expensetrackerapp.Database.Budget;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.expensetrackerapp.Database.AppDatabase;

import java.util.concurrent.Executors;

/**
 * Repository class for managing Budget data.
 * - Acts as a single source of truth for accessing budgets.
 * - Handles database operations in a background thread to avoid blocking the UI.
 */
public class BudgetRepository {

    private BudgetDao budgetDao;                   // DAO for budget-related database operations
    private LiveData<BudgetEntity> latestBudgetLive; // LiveData holding the latest budget

    /**
     * Constructor: Initializes DAO and LiveData
     * @param application Application context for database instance
     */
    public BudgetRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application); // Get singleton database instance
        budgetDao = db.budgetDao();                            // Initialize Budget DAO
        latestBudgetLive = budgetDao.getLatestBudgetLive();    // Fetch latest budget as LiveData
    }

    /**
     * Insert a new budget into the database
     * Runs on a background thread to prevent blocking the UI
     * @param budget BudgetEntity to insert
     */
    public void insert(BudgetEntity budget) {
        Executors.newSingleThreadExecutor().execute(() -> budgetDao.insertBudget(budget));
    }

    /**
     * Get LiveData of the latest budget
     * @return LiveData containing the latest BudgetEntity
     */
    public LiveData<BudgetEntity> getLatestBudgetLive() {
        return budgetDao.getLatestBudgetLive();
    }
}
