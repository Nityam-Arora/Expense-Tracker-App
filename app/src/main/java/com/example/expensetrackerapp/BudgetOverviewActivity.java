package com.example.expensetrackerapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerapp.Adapters.BudgetAdapter;
import com.example.expensetrackerapp.Database.Budget.BudgetEntity;
import com.example.expensetrackerapp.Database.Budget.BudgetViewModel;
import com.example.expensetrackerapp.Database.Expense.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity for getting the budget overview.
 *
 * Features:
 * - List of budgets
 * - Get list of total expenses category wise
 */

public class BudgetOverviewActivity extends AppCompatActivity {

    private final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget_overview);

        // ----------------------- View References -----------------------
        RecyclerView rvBudget = findViewById(R.id.rvBudget);
        FloatingActionButton fabAddBudget = findViewById(R.id.fabAddBudget);
        TextView noData = findViewById(R.id.noData);

        BudgetViewModel budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // ----------------------- Budget -----------------------
        fabAddBudget.setOnClickListener(v -> {
            View addBudgetPopup = LayoutInflater.from(BudgetOverviewActivity.this).inflate(R.layout.add_budget_layout, null);
            EditText addBudget = addBudgetPopup.findViewById(R.id.addBudget);
            Button addButton = addBudgetPopup.findViewById(R.id.addButton);

            AlertDialog.Builder builder = new AlertDialog.Builder(BudgetOverviewActivity.this);
            builder.setView(addBudgetPopup);

            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            // Add Budget
            addButton.setOnClickListener(v1 -> {
                String budgetText = addBudget.getText().toString().trim();

                if (!budgetText.isEmpty()) {
                    try {
                        // Remove everything except digits
                        String cleanText = budgetText.replaceAll("[^0-9]", "");

                        // If cleanText is empty after cleaning, stop
                        if (cleanText.isEmpty()) {
                            Toast.makeText(BudgetOverviewActivity.this, "Please enter a valid budget", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int budgetAmount = Integer.parseInt(cleanText);

                        // Check for zero or negative values
                        if (budgetAmount <= 0) {
                            Toast.makeText(BudgetOverviewActivity.this, "Budget must be greater than 0", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String date = dbDateFormat.format(new Date());
                        budgetViewModel.insert(new BudgetEntity(budgetAmount, date));

                        Toast.makeText(BudgetOverviewActivity.this, "Budget Saved: ₹" + budgetAmount, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } catch (NumberFormatException e) {
                        Toast.makeText(BudgetOverviewActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BudgetOverviewActivity.this, "Please enter your budget", Toast.LENGTH_SHORT).show();
                }
            });

        });

        // ----------------------- All added Budgets -----------------------
        rvBudget.setLayoutManager(new LinearLayoutManager(this));

        budgetViewModel.getAllBudget().observe(this, budgetEntities -> {
            if (budgetEntities == null || budgetEntities.isEmpty()) {
                // No Data
                rvBudget.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            } else {
                // Show Data
                rvBudget.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);

                BudgetAdapter adapter = new BudgetAdapter(this, budgetEntities, expenseViewModel);
                rvBudget.setAdapter(adapter);
            }
        });
    }
}