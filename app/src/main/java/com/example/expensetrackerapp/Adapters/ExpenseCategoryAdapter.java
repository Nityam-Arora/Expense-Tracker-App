package com.example.expensetrackerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerapp.Database.Expense.ExpenseDao;
import com.example.expensetrackerapp.R;

import java.util.List;

/**
 * RecyclerView Adapter for displaying total expenses grouped by category.
 *
 * Responsibilities:
 * - Inflate category summary layout for each item
 * - Bind category name and its total expense amount
 * - Show the data in a simple list format
 */
public class ExpenseCategoryAdapter extends RecyclerView.Adapter<ExpenseCategoryAdapter.ExpenseViewHolder> {

    private Context context;
    private List<ExpenseDao.CategoryTotal> categoryTotalList;

    /**
     * Constructor for initializing adapter with context and data list.
     *
     * @param context            The calling context (Activity/Fragment).
     * @param categoryTotalList  List of category-total pairs retrieved from DB.
     */
    public ExpenseCategoryAdapter(Context context, List<ExpenseDao.CategoryTotal> categoryTotalList) {
        this.context = context;
        this.categoryTotalList = categoryTotalList;
    }

    /**
     * Inflate the layout for each item in the RecyclerView.
     */
    @NonNull
    @Override
    public ExpenseCategoryAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_sum_template, parent, false);
        return new ExpenseViewHolder(view);
    }

    /**
     * Bind category and its total expense to the view.
     *
     * @param holder   ViewHolder for the current item.
     * @param position Position of the current item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseDao.CategoryTotal current = categoryTotalList.get(position);

        holder.category.setText(current.category);
        holder.amount.setText("₹ " + current.total);
    }

    /**
     * Return the total number of items in the list.
     */
    @Override
    public int getItemCount() {
        return categoryTotalList.size();
    }

    /**
     * ViewHolder class for holding category and total expense views.
     */
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        TextView amount;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.expenseCategory);
            amount = itemView.findViewById(R.id.expenseAmount);
        }
    }
}
