package com.example.expensetrackerapp.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class ExpenseRepository {
    private ExpenseDao expenseDao;
    private LiveData<List<ExpenseEntity>> allExpenses;

    public ExpenseRepository(Application application)
    {
        ExpenseDatabase db = ExpenseDatabase.getInstance(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
    }

    public void insert(ExpenseEntity expense)
    {
        Executors.newSingleThreadExecutor().execute(() -> expenseDao.insertExpense(expense));
    }

    public void delete(ExpenseEntity expense)
    {
        Executors.newSingleThreadExecutor().execute(() -> expenseDao.deleteExpense(expense));
    }

    public LiveData<List<ExpenseEntity>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<ExpenseEntity>> getLastThreeExpenses()
    {
        return expenseDao.getLastThreeExpenses();
    }
}
