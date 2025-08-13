package com.example.expensetrackerapp.Database.Budget;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BudgetDao {
    @Insert
    void insertBudget(BudgetEntity budget);

    @Query("SELECT * FROM budget ORDER BY id DESC LIMIT 1")
    LiveData<BudgetEntity> getLatestBudgetLive();
}
