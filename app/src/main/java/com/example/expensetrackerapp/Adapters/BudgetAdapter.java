package com.example.expensetrackerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerapp.Database.Budget.BudgetEntity;
import com.example.expensetrackerapp.Database.Expense.ExpenseViewModel;
import com.example.expensetrackerapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying a list of budgets grouped by month.
 * Each budget shows a nested RecyclerView of category totals
 * only for the respective month.
 */
public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private final Context context;
    private final List<BudgetEntity> budgetList;
    private final ExpenseViewModel expenseViewModel;

    public BudgetAdapter(Context context, List<BudgetEntity> budgetList, ExpenseViewModel expenseViewModel) {
        this.context = context;
        this.budgetList = budgetList;
        this.expenseViewModel = expenseViewModel;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget_template, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetEntity budget = budgetList.get(position);

        // --- Formatters ---
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault()); // e.g. Sep 2025

        // --- Current Month-Year ---
        String currentMonthYear;
        try {
            currentMonthYear = monthYearFormat.format(dbDateFormat.parse(budget.getDate()));
        } catch (Exception e) {
            currentMonthYear = budget.getDate(); // fallback
        }

        // --- Show header only when month changes or first item ---
        if (position == 0) {
            holder.budgetDate.setVisibility(View.VISIBLE);
            holder.budgetDate.setText(currentMonthYear);
        } else {
            String prevMonthYear;
            try {
                prevMonthYear = monthYearFormat.format(dbDateFormat.parse(budgetList.get(position - 1).getDate()));
            } catch (Exception e) {
                prevMonthYear = budgetList.get(position - 1).getDate();
            }

            if (!currentMonthYear.equals(prevMonthYear)) {
                holder.budgetDate.setVisibility(View.VISIBLE);
                holder.budgetDate.setText(currentMonthYear);
            } else {
                holder.budgetDate.setVisibility(View.GONE);
            }
        }

        // --- Budget amount ---
        holder.budgetAmount.setText("₹ " + budget.getAmount());

        // --- Setup nested RecyclerView for category totals ---
        holder.rvTransactions.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        // Get start & end of month for this budget
        String[] range = getMonthRange(budget.getDate());
        String startDate = range[0];
        String endDate = range[1];

        // Observe only expenses in this month range
        expenseViewModel.getCategoryTotal(startDate, endDate)
                .observe((LifecycleOwner) context, categoryTotal -> {
                    ExpenseCategoryAdapter adapter =
                            new ExpenseCategoryAdapter(holder.itemView.getContext(), categoryTotal);
                    holder.rvTransactions.setAdapter(adapter);
                });

        // --- Show More / Show Less logic ---
        if (position == budgetList.size() - 1 || isNewMonth(position, dbDateFormat, monthYearFormat)) {
            holder.showMore.setVisibility(View.VISIBLE);
        } else {
            holder.showMore.setVisibility(View.GONE);
            holder.showLess.setVisibility(View.GONE);
        }

        holder.showMore.setOnClickListener(v -> {
            holder.showMore.setVisibility(View.GONE);
            holder.showLess.setVisibility(View.VISIBLE);
            holder.rvTransactions.setVisibility(View.VISIBLE);
        });

        holder.showLess.setOnClickListener(v -> {
            holder.showMore.setVisibility(View.VISIBLE);
            holder.showLess.setVisibility(View.GONE);
            holder.rvTransactions.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    /** Helper to get first and last day of the month for a given date string. */
    private String[] getMonthRange(String dateStr) {
        try {
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dbFormat.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            // Start of month
            cal.set(Calendar.DAY_OF_MONTH, 1);
            String start = dbFormat.format(cal.getTime());

            // End of month
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String end = dbFormat.format(cal.getTime());

            return new String[]{start, end};
        } catch (Exception e) {
            return new String[]{"", ""};
        }
    }

    /** Checks if next item belongs to a new month. */
    private boolean isNewMonth(int position, SimpleDateFormat dbDateFormat, SimpleDateFormat monthYearFormat) {
        try {
            String currentMonthYear = monthYearFormat.format(dbDateFormat.parse(budgetList.get(position).getDate()));
            String nextMonthYear = monthYearFormat.format(dbDateFormat.parse(budgetList.get(position + 1).getDate()));
            return !currentMonthYear.equals(nextMonthYear);
        } catch (Exception e) {
            return false;
        }
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView budgetDate, budgetAmount, showMore, showLess;
        RecyclerView rvTransactions;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            budgetDate = itemView.findViewById(R.id.headerDate);
            budgetAmount = itemView.findViewById(R.id.budgetAmount);
            showMore = itemView.findViewById(R.id.showMoreText);
            showLess = itemView.findViewById(R.id.showLessText);
            rvTransactions = itemView.findViewById(R.id.rvTransactions);
        }
    }
}
