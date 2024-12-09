package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activity_vital_signs extends AppCompatActivity {

    private EditText inputBP, inputTemp, inputSugar, inputWeight, inputHeight, inputOxygen, inputESR, inputHemoglobin;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs);

        // Initialize input fields
        inputBP = findViewById(R.id.input_bp);
        inputTemp = findViewById(R.id.input_temperature);
        inputSugar = findViewById(R.id.input_sugar);
        inputWeight = findViewById(R.id.input_weight);
        inputHeight = findViewById(R.id.input_height);
        inputOxygen = findViewById(R.id.input_oxygen);
        inputESR = findViewById(R.id.input_esr); // Optional
        inputHemoglobin = findViewById(R.id.input_hemoglobin); // Optional
        btnSubmit = findViewById(R.id.btn_submit);

        // Handle Submit button click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    Intent intent = new Intent(activity_vital_signs.this, activity_result.class);
                    intent.putExtra("bp", inputBP.getText().toString());
                    intent.putExtra("temperature", inputTemp.getText().toString());
                    intent.putExtra("sugar", inputSugar.getText().toString());
                    intent.putExtra("weight", inputWeight.getText().toString());
                    intent.putExtra("height", inputHeight.getText().toString());
                    intent.putExtra("oxygen", inputOxygen.getText().toString());
                    intent.putExtra("esr", inputESR.getText().toString()); // Pass optional ESR
                    intent.putExtra("hemoglobin", inputHemoglobin.getText().toString()); // Pass optional Hemoglobin
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(inputBP.getText().toString()) ||
                TextUtils.isEmpty(inputTemp.getText().toString()) ||
                TextUtils.isEmpty(inputSugar.getText().toString()) ||
                TextUtils.isEmpty(inputWeight.getText().toString()) ||
                TextUtils.isEmpty(inputHeight.getText().toString()) ||
                TextUtils.isEmpty(inputOxygen.getText().toString())) {
            Toast.makeText(this, "Please fill all mandatory fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

