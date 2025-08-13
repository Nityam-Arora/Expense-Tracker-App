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

import com.example.expensetrackerapp.Adapters.ExpenseAdapter;
import com.example.expensetrackerapp.Database.Expense.ExpenseViewModel;
import com.example.expensetrackerapp.R;

public class WeeklyFragment extends Fragment {

    RecyclerView rvTransactions;
    ExpenseViewModel expenseViewModel;
    ExpenseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTransactions = view.findViewById(R.id.rvTransactions);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvTransactions.setLayoutManager(layoutManager);

        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        expenseViewModel.getLastWeekExpenses().observe(getViewLifecycleOwner(), allExpenses -> {
            adapter = new ExpenseAdapter(getContext(), allExpenses, ExpenseAdapter.TYPE_MONTHLY_FRAGMENT);
            rvTransactions.setAdapter(adapter);
        });
    }
}