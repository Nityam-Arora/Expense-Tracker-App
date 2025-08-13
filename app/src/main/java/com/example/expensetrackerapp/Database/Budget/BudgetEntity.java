package com.example.expensetrackerapp.Database.Budget;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget")
public class BudgetEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int amount;
    private String date;

    public BudgetEntity(int amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
