package com.example.expensetrackerapp.Database.Budget;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.expensetrackerapp.Database.AppDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class BudgetRepository {
    private BudgetDao budgetDao;
    private LiveData<BudgetEntity> latestBudgetLive;

    public BudgetRepository(Application application)
    {
        AppDatabase db = AppDatabase.getInstance(application);
        budgetDao = db.budgetDao();
        latestBudgetLive = budgetDao.getLatestBudgetLive();
    }

    public void insert(BudgetEntity budget)
    {
        Executors.newSingleThreadExecutor().execute(() -> budgetDao.insertBudget(budget));
    }

    public LiveData<BudgetEntity> getLatestBudgetLive() { return budgetDao.getLatestBudgetLive(); }
}
