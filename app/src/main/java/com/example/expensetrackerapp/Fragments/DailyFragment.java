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
 * Fragment to show daily transactions.
 * - Retrieves the expense data from Room database via ViewModel.
 * - Displays today's date in user-friendly format.
 * - Populates RecyclerView with daily expenses using an adapter.
 */
public class DailyFragment extends Fragment {

    // Formatter to display the date in dd/MM/yyyy format
    private final SimpleDateFormat uiDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    RecyclerView rvTransactions;        // RecyclerView to show the list of expenses
    ExpenseViewModel expenseViewModel;  // ViewModel to fetch data from database
    ExpenseOverviewAdapter adapter;     // Adapter to bind expenses to RecyclerView

    TextView date;                      // TextView to display current date

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout XML for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get today's date formatted as dd/MM/yyyy
        String todayDate = uiDateFormat.format(new Date());

        // Initialize the TextView and set today's date
        date = view.findViewById(R.id.date);
        date.setText(todayDate);

        // Initialize RecyclerView and set a linear vertical layout manager
        rvTransactions = view.findViewById(R.id.rvTransactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the shared ViewModel instance to fetch today's expenses
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // Observe changes in today's expenses from the database
        expenseViewModel.getTodayExpenses().observe(getViewLifecycleOwner(), allExpenses -> {
            // Create and set adapter to populate RecyclerView with expenses
            adapter = new ExpenseOverviewAdapter(getContext(), allExpenses, ExpenseOverviewAdapter.TYPE_FRAGMENT);
            rvTransactions.setAdapter(adapter);
        });
    }
}
