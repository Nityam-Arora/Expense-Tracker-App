package com.example.expensetrackerapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.expensetrackerapp.Database.Budget.BudgetDao;
import com.example.expensetrackerapp.Database.Budget.BudgetEntity;
import com.example.expensetrackerapp.Database.Expense.ExpenseDao;
import com.example.expensetrackerapp.Database.Expense.ExpenseEntity;

/**
 * Main Room database class for the Expense Tracker app.
 * - Holds the database configuration and serves as the main access point.
 * - Includes ExpenseEntity and BudgetEntity tables.
 * - Provides DAOs to perform database operations.
 */
@Database(entities = {ExpenseEntity.class, BudgetEntity.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {

    // Singleton instance to ensure only one database instance exists across the app
    public static AppDatabase instance;

    // Abstract method to access ExpenseDao
    public abstract ExpenseDao expenseDao();

    // Abstract method to access BudgetDao
    public abstract BudgetDao budgetDao();

    /**
     * Returns the singleton instance of the database.
     * If it does not exist, it creates it using Room.databaseBuilder.
     *
     * @param context Application context
     * @return AppDatabase singleton instance
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            // Build the Room database
            instance = Room.databaseBuilder(
                            context.getApplicationContext(), // Ensure context is application-level
                            AppDatabase.class,               // Database class
                            "expense_database")              // Database name
                    .fallbackToDestructiveMigration() // Recreates database if version changes without migration
                    .build();
        }
        return instance;
    }
}
