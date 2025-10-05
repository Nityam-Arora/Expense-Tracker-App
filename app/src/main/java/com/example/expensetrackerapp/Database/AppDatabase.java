package com.example.expensetrackerapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

    // Define M7_8 when you update the version number to 8 in the @Database annotation
    public static final Migration M7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // SQL commands goes here to update the schema safely.
            // Example: database.execSQL("ALTER TABLE ExpenseEntity ADD COLUMN notes TEXT NOT NULL DEFAULT ''");
        }
    };

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
                    .addMigrations(M7_8) // e.g., if you change version to 8
                    .build();
        }
        return instance;
    }
}
