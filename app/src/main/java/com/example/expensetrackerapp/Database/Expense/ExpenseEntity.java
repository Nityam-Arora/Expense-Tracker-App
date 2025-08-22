package com.example.expensetrackerapp.Database.Expense;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing an Expense table in the Room database.
 * Each instance corresponds to a single expense entry.
 */
@Entity(tableName = "expenses")
public class ExpenseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;             // Unique ID for each expense, auto-generated

    private String date;        // Date of the expense (format: "dd/MM/yyyy")
    private String time;        // Time of the expense (format: "HH:mm")
    private String category;    // Category of the expense (e.g., Food, Travel, Bills)
    private int amount;         // Amount spent in this expense
    private String description; // Optional description or note about the expense

    /**
     * Constructor to create a new ExpenseEntity
     * @param date Date of the expense
     * @param time Time of the expense
     * @param category Category of the expense
     * @param amount Amount spent
     * @param description Optional description
     */
    public ExpenseEntity(String date, String time, String category, int amount, String description) {
        this.date = date;
        this.time = time;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    /** Getters and Setters */

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
