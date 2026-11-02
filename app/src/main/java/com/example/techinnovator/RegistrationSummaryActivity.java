package com.example.techinnovator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class RegistrationSummaryActivity extends AppCompatActivity {
    private Chronometer countdownTimer;
    private TextView countdownText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_summary);

        // Set toolbar title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Registration Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize views
        TextView nameSummary = findViewById(R.id.nameSummary);
        TextView emailSummary = findViewById(R.id.emailSummary);
        TextView registrationTypeSummary = findViewById(R.id.registrationTypeSummary);
        TextView teamNameSummary = findViewById(R.id.teamNameSummary);
        TextView projectCategorySummary = findViewById(R.id.projectCategorySummary);
        countdownText = findViewById(R.id.countdownText);
        countdownTimer = findViewById(R.id.countdownTimer);

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("HackathonPrefs", MODE_PRIVATE);

        // Display registration details
        nameSummary.setText("Name: " + sharedPreferences.getString("name", ""));
        emailSummary.setText("Email: " + sharedPreferences.getString("email", ""));

        boolean isTeam = sharedPreferences.getBoolean("isTeam", true);
        registrationTypeSummary.setText("Registration Type: " + (isTeam ? "Team" : "Individual"));

        if (isTeam) {
            String teamName = sharedPreferences.getString("teamName", "");
            teamNameSummary.setVisibility(View.VISIBLE);
            teamNameSummary.setText("Team Name: " + (teamName.isEmpty() ? "Not specified" : teamName));
        }

        String[] categories = getResources().getStringArray(R.array.project_categories);
        int categoryPos = sharedPreferences.getInt("projectCategory", 0);
        projectCategorySummary.setText("Project Category: " + categories[categoryPos]);

        // Setup countdown timer
        setupCountdownTimer();
    }

    @SuppressLint("SetTextI18n")
    private void setupCountdownTimer() {
        // Set Hackathon start date (example: 1 week from now)
        Calendar hackathonDate = Calendar.getInstance();
        hackathonDate.add(Calendar.WEEK_OF_YEAR, 1);

        long hackathonTime = hackathonDate.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        long timeLeft = hackathonTime - currentTime;

        if (timeLeft > 0) {
            countdownText.setText("Time until Hackathon starts:");
            countdownTimer.setBase(SystemClock.elapsedRealtime() + timeLeft);
            countdownTimer.setFormat("%D days %H:%M:%S");
            countdownTimer.start();
        } else {
            countdownText.setText("Hackathon has started!");
            countdownTimer.stop();
            countdownTimer.setText("00:00:00");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.summary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            startActivity(new Intent(this, activity_registration_form.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}