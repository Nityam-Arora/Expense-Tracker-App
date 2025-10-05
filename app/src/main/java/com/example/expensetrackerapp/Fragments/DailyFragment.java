package com.example.expensetrackerapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensetrackerapp.Adapters.ExpenseOverviewAdapter;
import com.example.expensetrackerapp.Database.Expense.ExpenseViewModel;
import com.example.expensetrackerapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.text.SimpleDateFormat;
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

    TextView tvNoData;

    AdView adView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout XML for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and set a linear vertical layout manager
        rvTransactions = view.findViewById(R.id.rvTransactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        adView = view.findViewById(R.id.adView);
        if (adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                adView.postDelayed(() -> adView.loadAd(new AdRequest.Builder().build()), 5000);
            }
        });


        tvNoData = view.findViewById(R.id.tvNoData);

        // Get the shared ViewModel instance to fetch today's expenses
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // Observe changes in today's expenses from the database
        expenseViewModel.getTodayExpenses().observe(getViewLifecycleOwner(), allExpenses -> {
            if (allExpenses == null || allExpenses.isEmpty()) {
                tvNoData.setVisibility(View.VISIBLE);
                rvTransactions.setVisibility(View.GONE);
            } else {
                tvNoData.setVisibility(View.GONE);
                rvTransactions.setVisibility(View.VISIBLE);

                // Create and set adapter to populate RecyclerView with expenses
                adapter = new ExpenseOverviewAdapter(getContext(), allExpenses, ExpenseOverviewAdapter.TYPE_FRAGMENT);
                rvTransactions.setAdapter(adapter);
            }
        });
    }

    // AdView lifecycle handling to prevent leaks
    @Override
    public void onPause() {
        super.onPause();
        if (adView != null) adView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adView != null) adView.destroy();
    }
}
