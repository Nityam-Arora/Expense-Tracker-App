package com.example.expensetrackerapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.expensetrackerapp.Fragments.DailyFragment;
import com.example.expensetrackerapp.Fragments.MonthlyFragment;
import com.example.expensetrackerapp.Fragments.WeeklyFragment;

/**
 * ViewPager2 Adapter for managing and switching between expense summary fragments.
 *
 * Responsibilities:
 * - Provide fragments for daily, weekly, and monthly views
 * - Handle fragment creation based on position
 * - Define the total number of fragments
 */
public class ViewPagerAdapter extends FragmentStateAdapter {

    /**
     * Constructor for initializing the adapter with a FragmentActivity.
     *
     * @param fragmentActivity The hosting activity for the fragments.
     */
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Create the fragment for a given position in the ViewPager.
     *
     * @param position Index of the fragment to display.
     * @return The corresponding fragment instance (Daily, Weekly, Monthly).
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new DailyFragment();
            case 1: return new WeeklyFragment();
            case 2: return new MonthlyFragment();
            default: return new WeeklyFragment(); // Fallback case
        }
    }

    /**
     * Return the total number of fragments to be displayed.
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}
