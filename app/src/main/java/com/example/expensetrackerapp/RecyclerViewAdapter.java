package com.example.expensetrackerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerapp.Database.ExpenseEntity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ExpenseViewHolder>{

    Context context;
    List<ExpenseEntity> expenseList;

    public RecyclerViewAdapter(Context context, List<ExpenseEntity> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_transaction_template, parent, false);
        return new ExpenseViewHolder(view);
    }

    // Binds the data to the template views

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ExpenseViewHolder holder, int position) {
        ExpenseEntity currentExpense = expenseList.get(position);
        holder.expenseDate.setText(currentExpense.getDate());
        holder.expenseCategory.setText(currentExpense.getCategory());
        holder.expenseAmount.setText(String.valueOf(currentExpense.getAmount()));
        holder.expenseDescription.setText(currentExpense.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Expenses Overview", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // Gets reference of the template views

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseDate;
        TextView expenseCategory;
        TextView expenseAmount;
        TextView expenseDescription;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseDate = itemView.findViewById(R.id.expenseDate);
            expenseCategory = itemView.findViewById(R.id.expenseCategory);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
        }
    }
}
