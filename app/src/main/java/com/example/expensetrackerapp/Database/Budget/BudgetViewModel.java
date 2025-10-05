package com.example.expensetrackerapp.Database.Budget;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * ViewModel class for managing Budget data.
 * - Acts as a bridge between the UI and the BudgetRepository.
 * - Provides LiveData for observing the latest budget in real-time.
 */
public class BudgetViewModel extends AndroidViewModel {

    private BudgetRepository repository;           // Repository to handle database operations
    private LiveData<BudgetEntity> latestBudget;   // LiveData holding the latest budget

    /**
     * Constructor: Initializes the repository and fetches the latest budget
     * @param application Application context
     */
    public BudgetViewModel(@NonNull Application application) {
        super(application);
        repository = new BudgetRepository(application);      // Initialize repository
        latestBudget = repository.getLatestBudgetLive();     // Get LiveData for latest budget
    }

    /**
     * Get LiveData of the latest budget
     * @return LiveData containing the latest BudgetEntity
     */
    public LiveData<BudgetEntity> getLatestBudgetLive() {
        return latestBudget;
    }

    /**
     * Insert a new budget into the database
     * @param budgetEntity BudgetEntity object to insert
     */
    public void insert(BudgetEntity budgetEntity) {
        repository.insert(budgetEntity);
    }

    public LiveData<List<BudgetEntity>> getAllBudget() { return repository.getAllBudget(); }
}
