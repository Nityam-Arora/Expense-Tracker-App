package com.example.expensetrackerapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetrackerapp.Database.Expense.ExpenseEntity;
import com.example.expensetrackerapp.Database.Expense.ExpenseViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity for adding a new expense record.
 *
 * Features:
 * - Select category from a predefined dropdown list
 * - Pick date (disallows future dates)
 * - Pick time (only shown if expense date is not today)
 * - Enter amount, description
 * - Saves expense in DB with normalized date (yyyy-MM-dd) and time (HH:mm:ss)
 */
public class AddExpenseActivity extends AppCompatActivity {

    // Date formats: UI vs Database
    private final SimpleDateFormat uiDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // Time formats: UI vs Database
    private final SimpleDateFormat uiFormatTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private final SimpleDateFormat dbFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);

        // ----------------------- View References -----------------------
        EditText addAmount = findViewById(R.id.addAmount);
        EditText addDate = findViewById(R.id.addDate);
        EditText addDescription = findViewById(R.id.addDescription);
        EditText addTime = findViewById(R.id.addTime);

        MaterialAutoCompleteTextView addCategory = findViewById(R.id.addCategory);
        MaterialButton saveButton = findViewById(R.id.saveButton);
        MaterialButton cancelButton = findViewById(R.id.cancelButton);
        TextInputLayout addTimeLayout = findViewById(R.id.addTimeLayout);

        // Hide time picker by default; will show if user selects a past date
        addTimeLayout.setVisibility(View.GONE);

        // ----------------------- Category Dropdown -----------------------
        String[] categories = new String[]{
                "Food", "Transport", "Shopping", "Bills",
                "Entertainment", "Medical", "Rent", "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, categories
        );

        addCategory.setAdapter(adapter);
        addCategory.setText("Food", false); // default selection
        addCategory.setKeyListener(null);   // make dropdown non-editable

        // ----------------------- Date Picker -----------------------
        String currentDate = uiDateFormat.format(new Date());
        addDate.setText(currentDate);

        Calendar calendar = Calendar.getInstance();
        long todayMillis = calendar.getTimeInMillis();

        addDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(todayMillis) // default = today
                    .setCalendarConstraints(
                            new CalendarConstraints.Builder()
                                    .setEnd(todayMillis) // prevent future dates
                                    .build()
                    )
                    .build();

            datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                if (selection > todayMillis) {
                    Toast.makeText(this, "Cannot select future dates", Toast.LENGTH_SHORT).show();
                    return;
                }

                String formattedDate = uiDateFormat.format(new Date(selection));
                addDate.setText(formattedDate);

                // Show/hide time picker based on date
                String todayDate = uiDateFormat.format(new Date(todayMillis));
                if (formattedDate.equals(todayDate)) {
                    addTimeLayout.setVisibility(View.GONE);
                    addTime.setText(uiFormatTime.format(new Date())); // reset to current time
                } else {
                    addTimeLayout.setVisibility(View.VISIBLE);
                }
            });
        });

        // ----------------------- Time Picker -----------------------
        // Default: Current time in hh:mm a format
        addTime.setText(uiFormatTime.format(new Date()));

        addTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddExpenseActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        selectedTime.set(Calendar.MINUTE, selectedMinute);

                        // Show in UI format
                        addTime.setText(uiFormatTime.format(selectedTime.getTime()));
                    },
                    hour, minute, false // false = 12-hour format
            );

            timePickerDialog.show();
        });

        // ----------------------- Save Button -----------------------
        saveButton.setOnClickListener(v -> {
            // Collect user input
            String amount = addAmount.getText().toString().replace("₹", "").trim();
            String category = addCategory.getText().toString().trim();
            String uiDate = addDate.getText().toString().trim();
            String description = addDescription.getText().toString().trim();
            String uiTime = addTime.getText().toString().trim();

            // ---------------- Validation ----------------
            if (amount.isEmpty()) {
                Toast.makeText(this, "Please enter Amount", Toast.LENGTH_SHORT).show();
                return;
            }

            int amountInt;
            try {
                amountInt = Integer.parseInt(amount);
                if (amountInt <= 0) {
                    Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert date from UI → DB format
            String dbDate;
            try {
                Date parsedDate = uiDateFormat.parse(uiDate);
                dbDate = dbDateFormat.format(parsedDate);
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert time from UI → DB format
            String dbTime;
            try {
                Date parsedTime = uiFormatTime.parse(uiTime);
                dbTime = dbFormatTime.format(parsedTime);
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show();
                return;
            }

            // ---------------- Create ExpenseEntity ----------------
            ExpenseEntity expense = new ExpenseEntity(dbDate, dbTime, category, amountInt, description);

            // Insert into DB via ViewModel
            ExpenseViewModel viewModel = new ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
            ).get(ExpenseViewModel.class);
            viewModel.insert(expense);

            Toast.makeText(this, "Expense Saved", Toast.LENGTH_SHORT).show();
            finish(); // Close activity
        });

        // ----------------------- Cancel Button -----------------------
        cancelButton.setOnClickListener(v -> {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
