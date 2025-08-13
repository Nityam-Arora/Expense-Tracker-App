package com.example.expensetrackerapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.expensetrackerapp.Database.Budget.BudgetDao;
import com.example.expensetrackerapp.Database.Budget.BudgetEntity;
import com.example.expensetrackerapp.Database.Expense.ExpenseDao;
import com.example.expensetrackerapp.Database.Expense.ExpenseEntity;

@Database(entities = {ExpenseEntity.class, BudgetEntity.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase instance;

    public abstract ExpenseDao expenseDao();
    public abstract BudgetDao budgetDao();

    public static synchronized AppDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "expense_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
