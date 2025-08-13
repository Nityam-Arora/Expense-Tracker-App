package com.example.expensetrackerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerapp.Database.Expense.ExpenseEntity;
import com.example.expensetrackerapp.ExpenseOverviewActivity;
import com.example.expensetrackerapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    List<ExpenseEntity> expenseList;

    public static final int TYPE_HOME = 0;
    public static final int TYPE_DAILY_FRAGMENT = 1;
    public static final int TYPE_WEEKLY_FRAGMENT = 2;
    public static final int TYPE_MONTHLY_FRAGMENT = 3;

    private int layoutType;

    public ExpenseAdapter(Context context, List<ExpenseEntity> expenseList, int layoutType) {
        this.context = context;
        this.expenseList = expenseList;
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (layoutType == TYPE_HOME) {
            View view = inflater.inflate(R.layout.display_last_transaction_template, parent, false);
            return new HomeViewHolder(view);
        } else if (layoutType == TYPE_DAILY_FRAGMENT) {
            View view = inflater.inflate(R.layout.fragment_transaction_template, parent, false);
            return new DailyViewHolder(view);
        } else if (layoutType == TYPE_WEEKLY_FRAGMENT) {
            View view = inflater.inflate(R.layout.fragment_transaction_template, parent, false);
            return new WeeklyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.fragment_transaction_template, parent, false);
            return new MonthlyViewHolder(view);
        }
    }

    // Binds the data to the template views

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExpenseEntity currentExpense = expenseList.get(position);

        // Date formats
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat uiFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Home activity
        if (layoutType == TYPE_HOME) {
            HomeViewHolder homeHolder = (HomeViewHolder) holder;

            // Convert stored DB date to UI date
            try {
                Date date = dbFormat.parse(currentExpense.getDate());
                homeHolder.expenseDate.setText(uiFormat.format(date));
            } catch (Exception e) {
                homeHolder.expenseDate.setText(currentExpense.getDate());
            }

            homeHolder.expenseCategory.setText(currentExpense.getCategory());
            homeHolder.expenseAmount.setText("₹ " + currentExpense.getAmount());

            if (currentExpense.getDescription() == null || currentExpense.getDescription().trim().isEmpty()) {
                homeHolder.expenseDescription.setVisibility(View.GONE);
            } else {
                homeHolder.expenseDescription.setVisibility(View.VISIBLE);
                homeHolder.expenseDescription.setText(currentExpense.getDescription());
            }

            homeHolder.itemView.setOnClickListener(v -> {
                Toast.makeText(context, "Expenses Overview", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, ExpenseOverviewActivity.class));
            });
        }

        // Daily fragment
        else if (layoutType == TYPE_DAILY_FRAGMENT) {
            DailyViewHolder dailyHolder = (DailyViewHolder) holder;

            dailyHolder.expenseTime.setText(currentExpense.getTime());
            dailyHolder.expenseCategory.setText(currentExpense.getCategory());
            dailyHolder.expenseAmount.setText("₹ " + currentExpense.getAmount());

            if (currentExpense.getDescription() == null || currentExpense.getDescription().trim().isEmpty()) {
                dailyHolder.expenseDescription.setVisibility(View.GONE);
            } else {
                dailyHolder.expenseDescription.setVisibility(View.VISIBLE);
                dailyHolder.expenseDescription.setText(currentExpense.getDescription());
            }
        }

        // Weekly fragment
        else if (layoutType == TYPE_WEEKLY_FRAGMENT) {
            WeeklyViewHolder weeklyHolder = (WeeklyViewHolder) holder;

            weeklyHolder.expenseTime.setText(currentExpense.getTime());
            weeklyHolder.expenseCategory.setText(currentExpense.getCategory());
            weeklyHolder.expenseAmount.setText("₹ " + currentExpense.getAmount());

            if (currentExpense.getDescription() == null || currentExpense.getDescription().trim().isEmpty()) {
                weeklyHolder.expenseDescription.setVisibility(View.GONE);
            } else {
                weeklyHolder.expenseDescription.setVisibility(View.VISIBLE);
                weeklyHolder.expenseDescription.setText(currentExpense.getDescription());
            }
        }

        // Monthly fragment
        else {
            MonthlyViewHolder monthlyHolder = (MonthlyViewHolder) holder;

            monthlyHolder.expenseTime.setText(currentExpense.getTime());
            monthlyHolder.expenseCategory.setText(currentExpense.getCategory());
            monthlyHolder.expenseAmount.setText("₹ " + currentExpense.getAmount());

            if (currentExpense.getDescription() == null || currentExpense.getDescription().trim().isEmpty()) {
                monthlyHolder.expenseDescription.setVisibility(View.GONE);
            } else {
                monthlyHolder.expenseDescription.setVisibility(View.VISIBLE);
                monthlyHolder.expenseDescription.setText(currentExpense.getDescription());
            }
        }
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // Gets reference of the template views

    // ViewHolder for Home layout
    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView expenseDate;
        TextView expenseCategory;
        TextView expenseAmount;
        TextView expenseDescription;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseDate = itemView.findViewById(R.id.expenseDate);
            expenseCategory = itemView.findViewById(R.id.expenseCategory);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
        }
    }

    // ViewHolder for Daily fragment layout
    public static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView expenseTime;
        TextView expenseCategory;
        TextView expenseAmount;
        TextView expenseDescription;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTime = itemView.findViewById(R.id.expenseTime);
            expenseCategory = itemView.findViewById(R.id.expenseCategory);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
        }
    }

    // ViewHolder for Weekly fragment layout
    public static class WeeklyViewHolder extends RecyclerView.ViewHolder {
        TextView expenseTime;
        TextView expenseCategory;
        TextView expenseAmount;
        TextView expenseDescription;

        public WeeklyViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTime = itemView.findViewById(R.id.expenseTime);
            expenseCategory = itemView.findViewById(R.id.expenseCategory);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
        }
    }

    // ViewHolder for Monthly fragment layout
    public static class MonthlyViewHolder extends RecyclerView.ViewHolder {
        TextView expenseTime;
        TextView expenseCategory;
        TextView expenseAmount;
        TextView expenseDescription;

        public MonthlyViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseTime = itemView.findViewById(R.id.expenseTime);
            expenseCategory = itemView.findViewById(R.id.expenseCategory);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
        }
    }
}