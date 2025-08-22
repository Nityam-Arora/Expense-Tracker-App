package com.example.expensetrackerapp.Database.Budget;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a Budget table in the Room database.
 * Each instance of this class corresponds to a single budget entry.
 */
@Entity(tableName = "budget")
public class BudgetEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;                 // Unique ID for each budget entry, auto-generated

    private int amount;             // Total budget amount set by the user
    private int remainingAmount;    // Remaining budget after expenses
    private String date;            // Date when the budget was created or added (format: "dd/MM/yyyy")

    /**
     * Constructor to create a new BudgetEntity
     * @param amount Total budget amount
     * @param date Date of the budget
     */
    public BudgetEntity(int amount, String date) {
        this.amount = amount;
        this.remainingAmount = amount;  // Initially, remainingAmount equals total amount
        this.date = date;
    }

    /** Getters and Setters */

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public int getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(int remainingAmount) { this.remainingAmount = remainingAmount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
