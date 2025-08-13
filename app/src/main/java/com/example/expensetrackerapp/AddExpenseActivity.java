package com.example.expensetrackerapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.ParcelFormatException;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.versionedparcelable.VersionedParcel;

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
import java.util.TimeZone;

public class AddExpenseActivity extends AppCompatActivity {

    private final SimpleDateFormat uiFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);

        //<editor-fold desc = "Views">

        EditText addAmount = findViewById(R.id.addAmount);
        EditText addDate = findViewById(R.id.addDate);
        EditText addDescription = findViewById(R.id.addDescription);
        EditText addTime = findViewById(R.id.addTime);

        MaterialAutoCompleteTextView addCategory = findViewById(R.id.addCategory);
        MaterialButton saveButton = findViewById(R.id.saveButton);
        MaterialButton cancleButton = findViewById(R.id.cancelButton);

        TextInputLayout addTimeLayout = findViewById(R.id.addTimeLayout);

        addTimeLayout.setVisibility(View.GONE);

        //</editor-fold>

        // <editor-fold desc = "Categories">

        String[] Categories = new String[] {
                "Food","Transport","Shopping","Bills","Entertainment","Medical","Rent","Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                Categories);
        addCategory.setAdapter(adapter);
        addCategory.setText("Food", false);
        addCategory.setKeyListener(null);
        addCategory.setOnItemClickListener((parent, view, position, id) -> {
            String SelectedCategory = parent.getItemAtPosition(position).toString();
        });

        // </editor-fold>

        // <editor-fold desc = "Date">

        String currentDate = uiFormat.format(new Date());
        addDate.setText(currentDate);

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

                if (selection > todayMillis){
                    Toast.makeText(this, "Cannot select future dates", Toast.LENGTH_SHORT).show();
                    return;
                }

                String formattedDate = uiFormat.format(new Date(selection));
                addDate.setText(formattedDate);

                String todayDate = uiFormat.format(new Date(todayMillis));
                if (formattedDate.equals(todayDate)) {
                    addTimeLayout.setVisibility(View.GONE);
                } else {
                    addTimeLayout.setVisibility(View.VISIBLE);
                }
            });
        });

        // </editor-fold>

        // <editor-fold desc = "Time">

        addTime.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date()));

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddExpenseActivity.this,
                        (view, selectedHour, selectedMinute) -> {
                            // Format to 12-hour with AM/PM
                            Calendar selectedTime = Calendar.getInstance();
                            selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                            selectedTime.set(Calendar.MINUTE, selectedMinute);

                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            addTime.setText(timeFormat.format(selectedTime.getTime()));
                        },
                        hour, minute, false // false = 12-hour format
                );

                timePickerDialog.show();
            }
        });

        // </editor-fold>

        // <editor-fold desc = "Save button">

        saveButton.setOnClickListener(v -> {
            String amount = addAmount.getText().toString().replace("₹","").trim();
            String category = addCategory.getText().toString().trim();
            String uiDate = addDate.getText().toString().trim();
            String description = addDescription.getText().toString().trim();

            String time = addTime.getText().toString().trim();

            if (amount.isEmpty()) {
                Toast.makeText(AddExpenseActivity.this, "Please enter Amount", Toast.LENGTH_SHORT).show();
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

            // Convert UI date to DB date format (yyyy-MM-dd)
            String dbDate;
            try {
                Date parsedDate = uiFormat.parse(uiDate);
                dbDate = dbFormat.format(parsedDate);
            } catch (ParseException e) {
                Toast.makeText(AddExpenseActivity.this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            ExpenseEntity expense = new ExpenseEntity(dbDate, time, category, amountInt, description);

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

        // <editor-fold desc = "Cancel button">

        cancleButton.setOnClickListener(v -> {
            finish();
            Toast.makeText(AddExpenseActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
        });

        // </editor-fold>

    }
}