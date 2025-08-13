package com.example.expensetrackerapp.Database.Expense;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.expensetrackerapp.Database.AppDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class ExpenseRepository {
    private ExpenseDao expenseDao;
    private LiveData<List<ExpenseEntity>> allExpenses;

    public ExpenseRepository(Application application)
    {
        AppDatabase db = AppDatabase.getInstance(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
    }

    public void insert(ExpenseEntity expense)
    {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Insert new expense
            expenseDao.insertExpense(expense);

//            // Get all expenses ordered by oldest first
//            List<ExpenseEntity> all = expenseDao.getAllExpensesList();

//            // If more than 3, delete the oldest one
//            if (all.size() > 3) {
//                ExpenseEntity oldest = all.get(0);
//                expenseDao.deleteExpense(oldest);
//            }
        });
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

    public LiveData<List<ExpenseEntity>> getLastWeekExpenses()
    {
        return expenseDao.getLastWeekExpenses();
    }
}
