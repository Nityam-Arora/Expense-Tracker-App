package com.example.expensetrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerapp.Database.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        CardView budgetCard = findViewById(R.id.budgetCard);
        FloatingActionButton addExpense = findViewById(R.id.addExpense);
        RecyclerView recentTransactions = findViewById(R.id.recentTransactions);

        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

// TODO : Implement adding monthly budget via popup

        budgetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Add Budget", Toast.LENGTH_SHORT).show();
            }
        });


        recentTransactions.setLayoutManager(new LinearLayoutManager(this));

        // NOTE: Click handling for recent transactions is implemented inside RecyclerViewAdapter.java

        expenseViewModel.getLastThreeExpenses().observe(this, lastThree -> {
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, lastThree);
            recentTransactions.setAdapter(adapter);
        });

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddExpenseActivity.class));
                Toast.makeText(HomeActivity.this, "Add Expense", Toast.LENGTH_SHORT).show();
            }
        });
    }
}