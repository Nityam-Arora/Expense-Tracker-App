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
import java.util.Calendar;
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
    private final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    RecyclerView rvTransactions;        // RecyclerView to display list of expenses
    ExpenseViewModel expenseViewModel;  // ViewModel to fetch data from database
    ExpenseOverviewAdapter adapter;     // Adapter to bind expenses to RecyclerView

    TextView tvNoData;

    AdView adView;

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

        // Set a vertical linear layout manager for the RecyclerView
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

        // Get the shared ViewModel instance to fetch monthly expenses
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        Calendar cal = Calendar.getInstance();
        Date end = cal.getTime();

        // Start date (1st of current month)
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();

        String startDate = dbDateFormat.format(start);
        String endDate = dbDateFormat.format(end);

        // Observe changes in monthly expenses from the database
        expenseViewModel.getMonthlyExpenses(startDate, endDate).observe(getViewLifecycleOwner(), allExpenses -> {
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
