package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;

public class activity_result extends AppCompatActivity {

    private TextView tvDiseasePrediction, tvDietSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvDiseasePrediction = findViewById(R.id.tv_disease_prediction);
        tvDietSuggestions = findViewById(R.id.tv_diet_suggestions);

        // Get input data
        String bp = getIntent().getStringExtra("bp");
        String temperature = getIntent().getStringExtra("temperature");
        String sugar = getIntent().getStringExtra("sugar");
        String weight = getIntent().getStringExtra("weight");
        String height = getIntent().getStringExtra("height");
        String oxygen = getIntent().getStringExtra("oxygen");

        // Directly use the Fahrenheit input for predictions
        float tempFahrenheit = Float.parseFloat(temperature);

        // Predict health and display results
        String[] result = predictHealth(bp, tempFahrenheit, sugar, weight, height, oxygen);
        tvDiseasePrediction.setText("Predicted Diseases: " + result[0]);
        tvDietSuggestions.setText("Diet Suggestions:\n\n" + result[1]);
    }

    private String[] predictHealth(String bp, float tempFahrenheit, String sugar, String weight, String height, String oxygen) {
        StringBuilder diseases = new StringBuilder();
        HashSet<String> foodSet = new HashSet<>();
        HashSet<String> avoidSet = new HashSet<>();
        StringBuilder precautions = new StringBuilder();

        // Parse input values
        int sugarLevel = Integer.parseInt(sugar);
        int systolicBP = Integer.parseInt(bp.split("/")[0]);
        float bmi = calculateBMI(Float.parseFloat(weight), Float.parseFloat(height));

        // Flag to track if any disease is found
        boolean hasDisease = false;

        // Check for diseases and append suggestions
        if (sugarLevel > 140) {
            diseases.append("Diabetes\n");
            foodSet.add("Bitter gourd, spinach, broccoli, apples, berries, and nuts");
            avoidSet.add("Sugary drinks, white bread, and fried foods");
            precautions.append("Regular exercise, monitor sugar levels.\n");
            hasDisease = true;
        }
        if (systolicBP > 130) {
            diseases.append("Hypertension\n");
            foodSet.add("Bananas, leafy greens, tomatoes, avocados, and oatmeal");
            avoidSet.add("Processed foods, excessive salt, and caffeine");
            precautions.append("Manage stress, engage in regular exercise.\n");
            hasDisease = true;
        }
        if (tempFahrenheit > 99.5) { // Check for fever based on Fahrenheit
            diseases.append("Fever\n");
            foodSet.add("Citrus fruits, soups, herbal teas, and coconut water");
            avoidSet.add("Cold drinks, spicy foods, and heavy meals");
            precautions.append("Stay hydrated, get adequate rest.\n");
            hasDisease = true;
        }
        if (bmi > 25) {
            diseases.append("Overweight\n");
            foodSet.add("Whole grains, legumes, cucumbers, carrots, and berries");
            avoidSet.add("Fast food, sugary snacks, and trans fats");
            precautions.append("Follow a calorie-controlled diet, regular physical activity.\n");
            hasDisease = true;
        }
        if (Integer.parseInt(oxygen) < 95) {
            diseases.append("Low Oxygen Levels\n");
            foodSet.add("Pomegranate, beetroot, spinach, and citrus fruits");
            avoidSet.add("Smoking and exposure to polluted air");
            precautions.append("Practice deep breathing exercises, consult a doctor if necessary.\n");
            hasDisease = true;
        }

        // If no disease detected, indicate the person is healthy
        if (!hasDisease) {
            diseases.append("Healthy");
            foodSet.add("A balanced diet, including a variety of fruits and vegetables");
        }

        // Combine food and avoidance lists into a single string with proper spacing
        StringBuilder combinedDietSuggestions = new StringBuilder();
        combinedDietSuggestions.append("Foods:\n").append(String.join(", ", foodSet)).append("\n\n");
        combinedDietSuggestions.append("Avoid:\n").append(String.join(", ", avoidSet)).append("\n\n");
        combinedDietSuggestions.append("Precautions:\n").append(precautions);

        return new String[]{diseases.toString(), combinedDietSuggestions.toString()};
    }

    private float calculateBMI(float weight, float height) {
        // Calculate BMI (weight in kg / height in meters squared)
        height = height / 100; // Convert height from cm to meters
        return weight / (height * height);
    }
}
