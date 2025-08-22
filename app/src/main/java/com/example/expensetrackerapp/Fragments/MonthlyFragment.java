package com.example.expensetrackerapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensetrackerapp.Adapters.ExpenseOverviewAdapter;
import com.example.expensetrackerapp.Database.Expense.ExpenseViewModel;
import com.example.expensetrackerapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Fragment to show monthly transactions.
 * - Retrieves the expense data from Room database via ViewModel.
 * - Displays "From" and "To" dates in user-friendly formats.
 * - Populates RecyclerView with monthly expenses using an adapter.
 */
public class MonthlyFragment extends Fragment {

    // Formatter to display the date in dd/MM/yyyy format
    private final SimpleDateFormat uiDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    RecyclerView rvTransactions;        // RecyclerView to display list of expenses
    ExpenseViewModel expenseViewModel;  // ViewModel to fetch data from database
    ExpenseOverviewAdapter adapter;     // Adapter to bind expenses to RecyclerView

    TextView fromDate, toDate;          // TextViews to show date range (from/to)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout XML for this fragment
        return inflater.inflate(R.layout.fragment_monthly, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        rvTransactions = view.findViewById(R.id.rvTransactions);

        // Initialize "From" and "To" date TextViews
        fromDate = view.findViewById(R.id.fromDate);
        toDate = view.findViewById(R.id.toDate);

        // Set today's date as the default "From" date
        String todayDate = uiDateFormat.format(new Date());
        fromDate.setText(todayDate);

        // Set a vertical linear layout manager for the RecyclerView
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the shared ViewModel instance to fetch monthly expenses
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // Observe changes in monthly expenses from the database
        expenseViewModel.getMonthlyExpenses().observe(getViewLifecycleOwner(), allExpenses -> {
            // Create and set adapter to populate RecyclerView with expenses
            adapter = new ExpenseOverviewAdapter(getContext(), allExpenses, ExpenseOverviewAdapter.TYPE_FRAGMENT);
            rvTransactions.setAdapter(adapter);
        });
    }
}
