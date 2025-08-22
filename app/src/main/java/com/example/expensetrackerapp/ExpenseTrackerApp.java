package com.example.expensetrackerapp;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Custom Application class for the Expense Tracker app.
 * This class is instantiated before any other class when the application process is created.
 * It is useful for app-wide initialization tasks.
 */

public class ExpenseTrackerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Disable dark mode for the entire app.
        // MODE_NIGHT_NO ensures that the app always uses the light theme.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
