package com.example.expensetrackerapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.expensetrackerapp.Fragments.DailyFragment;
import com.example.expensetrackerapp.Fragments.MonthlyFragment;
import com.example.expensetrackerapp.Fragments.WeeklyFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0 : return new DailyFragment();
            case 1 : return new WeeklyFragment();
            case 2 : return new MonthlyFragment();
            default : return new WeeklyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
