package com.example.expensetrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerapp.Adapters.ExpenseOverviewAdapter;
import com.example.expensetrackerapp.Database.Budget.BudgetEntity;
import com.example.expensetrackerapp.Database.Budget.BudgetViewModel;
import com.example.expensetrackerapp.Database.Expense.ExpenseViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // ----------------------- First Launch Check -----------------------
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            showFirstTimePrivacyDialog(); // Privacy Policy dialog
            showFirstTimeGuide(); // TapTarget guide
            prefs.edit().putBoolean("isFirstLaunch", false).apply();
        }

        // ----------------------- View References -----------------------
        CardView budgetCard = findViewById(R.id.budgetCard);
        FloatingActionButton addExpense = findViewById(R.id.addExpense);
        RecyclerView recentTransactions = findViewById(R.id.recentTransactions);
        TextView totalAmount = findViewById(R.id.totalAmount);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        adView = findViewById(R.id.adView);

        ExpenseViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        BudgetViewModel budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        // ----------------------- Budget -----------------------
        budgetViewModel.getLatestBudgetLive().observe(this, budgetEntity -> {
            if (budgetEntity != null) {
                int totalBudget = budgetEntity.getAmount();
                int remaining = budgetEntity.getRemainingAmount();
                totalAmount.setText("₹ " + remaining);
                progressBar.setMax(totalBudget);
                progressBar.setProgress(remaining);
            } else {
                totalAmount.setText("₹ 0");
            }
        });

        budgetCard.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, BudgetOverviewActivity.class)));

        totalAmount.setOnClickListener(v -> {
            View addBudgetPopup = LayoutInflater.from(HomeActivity.this).inflate(R.layout.add_budget_layout, null);
            EditText addBudget = addBudgetPopup.findViewById(R.id.addBudget);
            Button addButton = addBudgetPopup.findViewById(R.id.addButton);

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setView(addBudgetPopup);

            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            addButton.setOnClickListener(v1 -> {
                String budgetText = addBudget.getText().toString().trim();
                if (!budgetText.isEmpty()) {
                    try {
                        String cleanText = budgetText.replaceAll("[^0-9]", "");
                        if (cleanText.isEmpty()) {
                            Toast.makeText(HomeActivity.this, "Please enter a valid budget", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int budgetAmount = Integer.parseInt(cleanText);
                        if (budgetAmount <= 0) {
                            Toast.makeText(HomeActivity.this, "Budget must be greater than 0", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String date = dbDateFormat.format(new Date());
                        budgetViewModel.insert(new BudgetEntity(budgetAmount, date));
                        Toast.makeText(HomeActivity.this, "Budget Saved: ₹" + budgetAmount, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } catch (NumberFormatException e) {
                        Toast.makeText(HomeActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Please enter your budget", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // ----------------------- Last Three Transactions -----------------------
        recentTransactions.setLayoutManager(new LinearLayoutManager(this));
        expenseViewModel.getLastThreeExpenses().observe(this, lastThree -> {
            ExpenseOverviewAdapter adapter = new ExpenseOverviewAdapter(this, lastThree, ExpenseOverviewAdapter.TYPE_HOME);
            recentTransactions.setAdapter(adapter);
        });

        // ----------------------- Add Expenses Button -----------------------
        addExpense.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddExpenseActivity.class));
            Toast.makeText(HomeActivity.this, "Add Expense", Toast.LENGTH_SHORT).show();
        });

        // ------------------------------- Ads -------------------------------
        loadBannerAd();
    }

    // ----------------------- First-time Privacy Policy -----------------------
    private void showFirstTimePrivacyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome to ExpenseTrackerApp");

        String message = "By using this app, you agree to our Privacy Policy.\n\nTap here to read.";

        final TextView msg = new TextView(this);
        msg.setText(message);
        msg.setTextSize(16f);
        msg.setTextColor(Color.BLACK);
        msg.setPadding(50, 40, 50, 40);
        msg.setClickable(true);
        msg.setOnClickListener(v -> {
            String url = "https://sites.google.com/view/bachatbuddy-privacy/home"; // your hosted URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        builder.setView(msg);
        builder.setPositiveButton("Continue", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    // ----------------------- TapTarget Guide -----------------------
    private void showFirstTimeGuide() {
        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.budgetCard),
                                        "Your Budget",
                                        "Tap here to set or view your budget.")
                                .outerCircleColor(R.color.purple)
                                .transparentTarget(true)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() { }
                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) { }
                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) { }
                });

        sequence.start();
    }

    // ----------------------- Load Ads -----------------------
    private void loadBannerAd() {
        if (adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    adView.postDelayed(() -> adView.loadAd(new AdRequest.Builder().build()), 5000);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null) adView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
            adView = null;
        }
        super.onDestroy();
    }
}
