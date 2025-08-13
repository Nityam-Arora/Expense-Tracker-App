package com.example.expensetrackerapp.Database.Budget;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BudgetViewModel extends AndroidViewModel {

    private BudgetRepository repository;
    private LiveData<BudgetEntity> latestBudget;

    public BudgetViewModel(@NonNull Application application) {
        super(application);
        repository = new BudgetRepository(application);
        latestBudget = repository.getLatestBudgetLive();
    }

    public LiveData<BudgetEntity> getLatestBudgetLive() {
        return latestBudget;
    }

    public void insert(BudgetEntity budgetEntity) {
        repository.insert(budgetEntity);
    }
}
