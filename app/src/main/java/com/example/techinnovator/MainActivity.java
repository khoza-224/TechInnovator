
package com.example.techinnovator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Button viewSummaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar title
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tech Innovators Hackathon");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("HackathonPrefs", MODE_PRIVATE);

        // Initialize views
        Button registerButton = findViewById(R.id.registerButton);
        viewSummaryButton = findViewById(R.id.viewSummaryButton);

        // Set button click listeners
        registerButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, activity_registration_form.class)));
viewSummaryButton.setOnClickListener(v -> startActivity(new Intent().setAction("your.custom.action")));
        viewSummaryButton.setOnClickListener(v -> {
            if (sharedPreferences.contains("name")) {
                startActivity(new Intent(MainActivity.this, RegistrationSummaryActivity.class));
            } else {
                // Show message if no registration exists
                viewSummaryButton= findViewById(R.id.action_view_summary);
                viewSummaryButton= findViewById(R.id.viewSummaryButton);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update view summary button visibility based on existing registration
        boolean hasRegistration = sharedPreferences.contains("name");
        viewSummaryButton.setVisibility(hasRegistration ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            // Show about dialog
            new android.app.AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("Tech Innovators Hackathon Registration App\nVersion 1.0")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}