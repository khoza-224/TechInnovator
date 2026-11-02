package com.example.techinnovator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_registration_form extends AppCompatActivity {

    private EditText nameEditText, emailEditText, teamNameEditText;
    private RadioGroup registrationTypeRadioGroup;
    private Spinner projectCategorySpinner;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("HackathonPrefs", MODE_PRIVATE);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        teamNameEditText = findViewById(R.id.teamNameEditText);
        registrationTypeRadioGroup = findViewById(R.id.registrationTypeRadioGroup);
        projectCategorySpinner = findViewById(R.id.projectCategorySpinner);
        Button submitButton = findViewById(R.id.submitButton);

        // Set up toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registration Form");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up project categories spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.project_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectCategorySpinner.setAdapter(adapter);

        // Load saved data if exists
        loadSavedData();

        // Handle registration type change
        registrationTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.teamRadioButton) {
                teamNameEditText.setVisibility(View.VISIBLE);
            } else {
                teamNameEditText.setVisibility(View.GONE);
            }
        });

        // Handle submit button click
        submitButton.setOnClickListener(v -> {
            if (validateForm()) {
                saveRegistrationData();
                startActivity(new Intent(activity_registration_form.this, RegistrationSummaryActivity.class));
                finish();
            }
        });
    }

    private boolean validateForm() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        boolean isTeam = registrationTypeRadioGroup.getCheckedRadioButtonId() == R.id.teamRadioButton;
        String teamName = teamNameEditText.getText().toString().trim();

        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            nameEditText.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return false;
        }

        if (isTeam && teamName.isEmpty()) {
            teamNameEditText.setError("Team name is required for team registration");
            teamNameEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void saveRegistrationData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", nameEditText.getText().toString().trim());
        editor.putString("email", emailEditText.getText().toString().trim());
        editor.putBoolean("isTeam", registrationTypeRadioGroup.getCheckedRadioButtonId() == R.id.teamRadioButton);
        editor.putString("teamName", teamNameEditText.getText().toString().trim());
        editor.putInt("projectCategory", projectCategorySpinner.getSelectedItemPosition());
        editor.apply();

        Toast.makeText(this, "Registration saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedData() {
        nameEditText.setText(sharedPreferences.getString("name", ""));
        emailEditText.setText(sharedPreferences.getString("email", ""));

        boolean isTeam = sharedPreferences.getBoolean("isTeam", false);
        if (isTeam) {
            registrationTypeRadioGroup.check(R.id.teamRadioButton);
            teamNameEditText.setVisibility(View.VISIBLE);
            teamNameEditText.setText(sharedPreferences.getString("teamName", ""));
        } else {
            registrationTypeRadioGroup.check(R.id.individualRadioButton);
        }

        int categoryPos = sharedPreferences.getInt("projectCategory", 0);
        projectCategorySpinner.setSelection(categoryPos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registration_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_view_summary) {
            if (sharedPreferences.contains("name")) {
                startActivity(new Intent(this, RegistrationSummaryActivity.class));
            } else {
                Toast.makeText(this, "Please complete registration first", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}