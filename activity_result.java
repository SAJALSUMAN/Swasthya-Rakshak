package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;

public class activity_result extends AppCompatActivity {

    private TextView tvDiseasePrediction, tvDietSuggestions;

    // Define global thresholds for ESR and hemoglobin
    private static final int ESR_MAX = 15; // Example max ESR value (modify as needed)
    private static final int ESR_MIN = 0;  // Example min ESR value (modify as needed)
    private static final float HEMOGLOBIN_MAX = 17.5f; // Example max hemoglobin value (male/female ranges differ)
    private static final float HEMOGLOBIN_MIN = 12.0f; // Example min hemoglobin value (male/female ranges differ)

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
        String esrInput = getIntent().getStringExtra("esr"); // Optional input
        String hemoglobinInput = getIntent().getStringExtra("hemoglobin"); // Optional input

        // Directly use the Fahrenheit input for predictions
        float tempFahrenheit = Float.parseFloat(temperature);

        // Predict health and display results
        String[] result = predictHealth(bp, tempFahrenheit, sugar, weight, height, oxygen, esrInput, hemoglobinInput);
        tvDiseasePrediction.setText("Predicted Diseases: " + result[0]);
        tvDietSuggestions.setText("Diet Suggestions:\n\n" + result[1]);
    }

    private String[] predictHealth(String bp, float tempFahrenheit, String sugar, String weight, String height, String oxygen, String esrInput, String hemoglobinInput) {
        StringBuilder diseases = new StringBuilder();
        HashSet<String> foodSet = new HashSet<>();
        HashSet<String> avoidSet = new HashSet<>();
        StringBuilder precautions = new StringBuilder();

        // Parse input values
        int sugarLevel = Integer.parseInt(sugar);
        int systolicBP = Integer.parseInt(bp.split("/")[0]);
        float bmi = calculateBMI(Float.parseFloat(weight), Float.parseFloat(height));

        // Optional input handling
        Integer esr = esrInput != null && !esrInput.isEmpty() ? Integer.parseInt(esrInput) : null;
        Float hemoglobin = hemoglobinInput != null && !hemoglobinInput.isEmpty() ? Float.parseFloat(hemoglobinInput) : null;

        boolean hasDisease = false;

        // Check for diseases and append suggestions
        if (sugarLevel > 140) {
            diseases.append("Diabetes (High Sugar)\n");
            foodSet.add("Bitter gourd, spinach, broccoli, apples, berries, and nuts");
            avoidSet.add("Sugary drinks, white bread, and fried foods");
            precautions.append("Regular exercise, monitor sugar levels.\n");
            hasDisease = true;
        } else if (sugarLevel < 70) {
            diseases.append("Low Sugar (Hypoglycemia)\n");
            foodSet.add("Bananas, oranges, yogurt, and whole-grain bread");
            avoidSet.add("Excessive sugary snacks and alcohol");
            precautions.append("Eat small, frequent meals; monitor sugar levels.\n");
            hasDisease = true;
        }

        if (systolicBP > 130) {
            diseases.append("Hypertension\n");
            foodSet.add("Bananas, leafy greens, tomatoes, avocados, and oatmeal");
            avoidSet.add("Processed foods, excessive salt, and caffeine");
            precautions.append("Manage stress, engage in regular exercise.\n");
            hasDisease = true;
        }

        if (tempFahrenheit > 99.5) {
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

        // Check for ESR
        if (esr != null) {
            if (esr > ESR_MAX) {
                diseases.append("High ESR (Inflammation)\n");
                foodSet.add("Green tea, turmeric, and foods rich in antioxidants");
                avoidSet.add("Processed meats, refined sugar, and fried foods");
                precautions.append("Consult a physician for further evaluation.\n");
                hasDisease = true;
            } else if (esr < ESR_MIN) {
                diseases.append("Low ESR (Possible blood disorder)\n");
                foodSet.add("Iron-rich foods like spinach and beans");
                avoidSet.add("Unhealthy processed foods");
                precautions.append("Consider regular blood tests and physician consultations.\n");
                hasDisease = true;
            }
        }

        // Check for hemoglobin
        if (hemoglobin != null) {
            if (hemoglobin > HEMOGLOBIN_MAX) {
                diseases.append("High Hemoglobin (Polycythemia)\n");
                foodSet.add("Foods that improve hydration like fruits and vegetables");
                avoidSet.add("Iron supplements unless prescribed");
                precautions.append("Stay hydrated and avoid smoking.\n");
                hasDisease = true;
            } else if (hemoglobin < HEMOGLOBIN_MIN) {
                diseases.append("Low Hemoglobin (Anemia)\n");
                foodSet.add("Iron-rich foods, vitamin B12 sources like eggs and dairy");
                avoidSet.add("Caffeine near meals");
                precautions.append("Increase iron intake and consult a physician.\n");
                hasDisease = true;
            }
        }

        if (!hasDisease) {
            diseases.append("Healthy");
            foodSet.add("A balanced diet, including a variety of fruits and vegetables");
        }

        // Combine food and avoidance lists into a single string
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

