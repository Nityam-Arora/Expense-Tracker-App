package com.example.expensetrackerapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerapp.Adapters.ExpenseCategoryAdapter;
import com.example.expensetrackerapp.Adapters.ExpenseOverviewAdapter;
import com.example.expensetrackerapp.Database.Budget.BudgetEntity;
import com.example.expensetrackerapp.Database.Budget.BudgetViewModel;
import com.example.expensetrackerapp.Database.Expense.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * HomeActivity is the main screen of the Expense Tracker app.
 *
 * Features:
 * - Displays the latest budget and a progress bar showing spending.
 * - Shows the last three recent transactions.
 * - Allows adding a new budget via a popup dialog.
 * - Allows navigating to AddExpenseActivity to add a new expense.
 *
 * Interactions:
 * - Observes ExpenseViewModel and BudgetViewModel to get LiveData updates.
 * - Uses RecyclerView with ExpenseOverviewAdapter to display recent transactions.
 * - Stores date in UI format "dd-MM-yyyy" when adding budget.
 */

public class HomeActivity extends AppCompatActivity {

    private final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // ----------------------- View References -----------------------
        CardView budgetCard = findViewById(R.id.budgetCard);
        FloatingActionButton addExpense = findViewById(R.id.addExpense);
        RecyclerView recentTransactions = findViewById(R.id.recentTransactions);
        TextView totalAmount = findViewById(R.id.totalAmount);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        RecyclerView budgetCardRecycler = findViewById(R.id.budgetCardRecycler);

        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        BudgetViewModel budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        // ----------------------- Budget -----------------------
        budgetViewModel.getLatestBudgetLive().observe(this, budgetEntity -> {
            if (budgetEntity != null){
                int totalBudget = budgetEntity.getAmount();
                int remaining = budgetEntity.getRemainingAmount();
                totalAmount.setText("₹ " + remaining);
                progressBar.setMax(totalBudget);
                progressBar.setProgress(remaining);
            }else {
                totalAmount.setText("₹ 0");
            }
        });

        // Budget Card
        budgetCard.setOnClickListener(v -> {
            View addBudgetPopup = LayoutInflater.from(HomeActivity.this).inflate(R.layout.add_budget_layout, null);
            EditText addBudget = addBudgetPopup.findViewById(R.id.addBudget);
            Button addButton = addBudgetPopup.findViewById(R.id.addButton);

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setView(addBudgetPopup);

            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            // Add Budget
            addButton.setOnClickListener(v1 -> {
                String budgetText = addBudget.getText().toString().trim();

                if (!budgetText.isEmpty()){
                    try {
                        String cleanText = budgetText.replaceAll("[^0-9]", "");
                        int budgetAmount = Integer.parseInt(cleanText);
                        String date = dbDateFormat.format(new Date());

                        budgetViewModel.insert(new BudgetEntity(budgetAmount, date));
                        Log.e("Home Activity", "today date" + date);

                        Toast.makeText(HomeActivity.this, "Budget Saved: " + budgetText, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Please enter your budget", Toast.LENGTH_SHORT).show();
                }
            });
        });

        budgetCardRecycler.setLayoutManager(new LinearLayoutManager(this));

        expenseViewModel.getCategoryTotal().observe(this, categoryTotal -> {
            ExpenseCategoryAdapter adapter = new ExpenseCategoryAdapter(this, categoryTotal);
            budgetCardRecycler.setAdapter(adapter);
        });

        // ----------------------- Last Three Transactions -----------------------
        // Upto 3 Recent Transactions with latest one on top
        recentTransactions.setLayoutManager(new LinearLayoutManager(this));

        // NOTE: Click handling for recent transactions is implemented inside ExpenseOverviewAdapter.java

        expenseViewModel.getLastThreeExpenses().observe(this, lastThree -> {
            ExpenseOverviewAdapter adapter = new ExpenseOverviewAdapter(this, lastThree, ExpenseOverviewAdapter.TYPE_HOME);
            recentTransactions.setAdapter(adapter);
        });

        // ----------------------- Add Expenses Button -----------------------
        addExpense.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddExpenseActivity.class));
            Toast.makeText(HomeActivity.this, "Add Expense", Toast.LENGTH_SHORT).show();
        });
    }
}