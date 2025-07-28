package com.example.expensetrackerapp.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "expenses")
public class ExpenseEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;
    private String category;
    private int amount;
    private String description;

    public ExpenseEntity(String date, String category, int amount, String description) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
