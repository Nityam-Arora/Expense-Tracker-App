package com.example.expensetrackerapp.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository repository;
    private LiveData<List<ExpenseEntity>> allExpenses;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
    }

    public void insert(ExpenseEntity expense)
    {
        repository.insert(expense);
    }

    public void delete(ExpenseEntity expense)
    {
        repository.delete(expense);
    }

    public LiveData<List<ExpenseEntity>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<ExpenseEntity>> getLastThreeExpenses() {
        return repository.getLastThreeExpenses();
    }
}
