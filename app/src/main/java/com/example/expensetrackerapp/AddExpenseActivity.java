package com.example.expensetrackerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetrackerapp.Database.ExpenseEntity;
import com.example.expensetrackerapp.Database.ExpenseViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.OnSelectionChangedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);

        //<editor-fold desc = "Views">

        EditText addAmount = findViewById(R.id.addAmount);
        AutoCompleteTextView addCategory = findViewById(R.id.addCategory);
        EditText addDate = findViewById(R.id.addDate);
        EditText addDescription = findViewById(R.id.addDescription);
        MaterialButton saveButton = findViewById(R.id.saveButton);
        MaterialButton cancleButton = findViewById(R.id.cancelButton);

        //</editor-fold>

        // <editor-fold desc = "Categories">

        String[] Categories = new String[] {
                "Food","Transport","Shopping","Bills","Entertainment","Medical","Rent","Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Categories);
        addCategory.setAdapter(adapter);
        addCategory.setOnItemClickListener((parent, view, position, id) -> {
            String SelectedCategory = parent.getItemAtPosition(position).toString();
        });

        // </editor-fold>

        // <editor-fold desc = "Calendar">

        Calendar calendar = Calendar.getInstance();
        long todayMillis = calendar.getTimeInMillis();

        addDate.setOnClickListener(v -> {

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(todayMillis) // default selection = today
                    .setCalendarConstraints(
                            new CalendarConstraints.Builder()
                                    .setEnd(todayMillis) // restrict to today or earlier
                                    .build()
                    )
                    .build();

            datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(new Date(selection));
                addDate.setText(formattedDate);
            });
        });

        // </editor-fold>

        // <editor-fold desc = "Save button">

        saveButton.setOnClickListener(v -> {
            String amount = addAmount.getText().toString().trim();
            String category = addCategory.getText().toString().trim();
            String date = addDate.getText().toString().trim();
            String description = addDescription.getText().toString().trim();

            if (amount.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(AddExpenseActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert string to integer for amount
            int amountInt;
            try {
                amountInt = Integer.parseInt(amount);
            } catch (NumberFormatException e) {
                Toast.makeText(AddExpenseActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            ExpenseEntity expense = new ExpenseEntity(date, category, amountInt, description);

            // Get ViewModel and insert
            ExpenseViewModel viewModel = new ViewModelProvider(
                    AddExpenseActivity.this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
            ).get(ExpenseViewModel.class);
            viewModel.insert(expense);


            Toast.makeText(AddExpenseActivity.this, "Expense Saved", Toast.LENGTH_SHORT).show();
            finish(); // Go back to previous activity
        });

        // </editor-fold>

        // <editor-fold desc = "Cancle button">

        cancleButton.setOnClickListener(v -> {
            finish();
            Toast.makeText(AddExpenseActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
        });

        // </editor-fold>

    }
}