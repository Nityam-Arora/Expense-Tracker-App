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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView adapter for showing expenses.
 *
 * Supports two modes:
 *  - TYPE_HOME → Used in Home activity (shows grouped dates, no time).
 *  - TYPE_FRAGMENT → Used in Daily/Fragment view (shows grouped dates + time).
 *
 * Features:
 *  - Groups expenses by date (shows header only for first item of each date).
 *  - Converts DB date (yyyy-MM-dd) → UI date (dd/MM/yyyy).
 *  - Converts DB time (HH:mm:ss) → UI time (hh:mm a).
 *  - Hides description when empty.
 */
public class ExpenseOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<ExpenseEntity> expenseList;
    private final int layoutType;

    // Adapter modes
    public static final int TYPE_HOME = 0;
    public static final int TYPE_FRAGMENT = 1;

    public ExpenseOverviewAdapter(Context context, List<ExpenseEntity> expenseList, int layoutType) {
        this.context = context;
        this.expenseList = expenseList;
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate same layout but different ViewHolders based on mode
        View view = inflater.inflate(R.layout.last_transaction_template, parent, false);
        return (layoutType == TYPE_HOME) ? new HomeViewHolder(view) : new FragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExpenseEntity currentExpense = expenseList.get(position);

        // Formatters for DB → UI
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat uiDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat dbTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat uiTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        // ---------- HOME MODE ----------
        if (layoutType == TYPE_HOME) {
            HomeViewHolder homeHolder = (HomeViewHolder) holder;

            // Show date header only for first item of a date
            if (position == 0 || !currentExpense.getDate().equals(expenseList.get(position - 1).getDate())) {
                homeHolder.headerDate.setVisibility(View.VISIBLE);
                try {
                    Date date = dbDateFormat.parse(currentExpense.getDate());
                    homeHolder.headerDate.setText(uiDateFormat.format(date));
                } catch (Exception e) {
                    homeHolder.headerDate.setText(currentExpense.getDate());
                }
            } else {
                homeHolder.headerDate.setVisibility(View.GONE);
            }

            // Home view doesn’t show time
            homeHolder.expenseTime.setVisibility(View.GONE);

            // Set category + amount
            homeHolder.expenseCategory.setText(currentExpense.getCategory());
            homeHolder.expenseAmount.setText("₹ " + currentExpense.getAmount());

            // Show/hide description
            if (currentExpense.getDescription() == null || currentExpense.getDescription().trim().isEmpty()) {
                homeHolder.expenseDescription.setVisibility(View.GONE);
            } else {
                homeHolder.expenseDescription.setVisibility(View.VISIBLE);
                homeHolder.expenseDescription.setText(currentExpense.getDescription());
            }

            // Click → open overview activity
            homeHolder.itemView.setOnClickListener(v -> {
                Toast.makeText(context, "Expenses Overview", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, ExpenseOverviewActivity.class));
            });
        }

        // ---------- FRAGMENT MODE ----------
        else {
            FragmentViewHolder dailyHolder = (FragmentViewHolder) holder;

            // Show date header only for first item of a date
            if (position == 0 || !currentExpense.getDate().equals(expenseList.get(position - 1).getDate())) {
                dailyHolder.headerDate.setVisibility(View.VISIBLE);
                try {
                    Date date = dbDateFormat.parse(currentExpense.getDate());
                    dailyHolder.headerDate.setText(uiDateFormat.format(date));
                } catch (Exception e) {
                    dailyHolder.headerDate.setText(currentExpense.getDate());
                }
            } else {
                dailyHolder.headerDate.setVisibility(View.GONE);
            }

            // Show formatted time
            try {
                Date time = dbTimeFormat.parse(currentExpense.getTime());
                dailyHolder.expenseTime.setText(uiTimeFormat.format(time));
            } catch (ParseException e) {
                dailyHolder.expenseTime.setText(currentExpense.getTime());
            }

            // Set category + amount
            dailyHolder.expenseCategory.setText(currentExpense.getCategory());
            dailyHolder.expenseAmount.setText("₹ " + currentExpense.getAmount());

            // Show/hide description
            if (currentExpense.getDescription() == null || currentExpense.getDescription().trim().isEmpty()) {
                dailyHolder.expenseDescription.setVisibility(View.GONE);
            } else {
                dailyHolder.expenseDescription.setVisibility(View.VISIBLE);
                dailyHolder.expenseDescription.setText(currentExpense.getDescription());
            }
        }
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ---------- VIEW HOLDERS ----------

    /** ViewHolder for Home activity (no time, grouped by date). */
    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView expenseTime, expenseCategory, expenseAmount, expenseDescription, headerDate;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTime = itemView.findViewById(R.id.expenseTime);
            expenseCategory = itemView.findViewById(R.id.expenseCategory);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
            headerDate = itemView.findViewById(R.id.headerDate);
        }
    }

    /** ViewHolder for Fragment/Daily view (shows time + date grouping). */
    public static class FragmentViewHolder extends RecyclerView.ViewHolder {
        TextView expenseTime, expenseCategory, expenseAmount, expenseDescription, headerDate;

        public FragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTime = itemView.findViewById(R.id.expenseTime);
            expenseCategory = itemView.findViewById(R.id.expenseCategory);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
            headerDate = itemView.findViewById(R.id.headerDate);
        }
    }
}
