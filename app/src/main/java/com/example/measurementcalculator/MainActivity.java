package com.example.measurementcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Spinner;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.lang.Long;

public class MainActivity extends AppCompatActivity implements OnEditorActionListener, OnItemSelectedListener {

    private TextView unitsDisplay;
    private TextView convertedUnits;
    private TextView convertedUnitsOutput;
    private EditText unitsInput;
    private Spinner conversionSpinner;
    private SharedPreferences savedValues;

    private int spinnerPosition = 0;
    private double units = 0.0;
    private Long storedUnits;
    private double miles = 0.0;
    private double kilometers = 0.0;
    private double inches = 0.0;
    private double centimeters = 0.0;
    private final double milesConversion = 1.6093;
    private final double kiloConversion = 0.6214;
    private final double inchesConversion = 2.54;
    private final double centiConversion = 0.3937;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unitsDisplay = (TextView) findViewById(R.id.units);
        convertedUnits = (TextView) findViewById(R.id.convertedUnits);
        convertedUnitsOutput = (TextView) findViewById(R.id.convertedUnitsOutput);
        unitsInput = (EditText) findViewById(R.id.unitsInput);
        conversionSpinner = (Spinner) findViewById(R.id.conversionSpinner);
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.conversionArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        conversionSpinner.setAdapter(adapter);

        unitsInput.setOnEditorActionListener(this);
        conversionSpinner.setOnItemSelectedListener(this);
    }

    public void convertAndDisplay(int position) {
        switch (position) {
            case 0:
                unitsDisplay.setText(String.format("%s", "Miles"));
                unitsInput.setHint("Enter Miles here");
                convertedUnits.setText(String.format("%s", "Kilometers"));
                kilometers = units * milesConversion;
                convertedUnitsOutput.setText(String.format("%s", kilometers));
                break;
            case 1:
                unitsDisplay.setText(String.format("%s", "Kilometers"));
                unitsInput.setHint("Enter Kilometers here");
                convertedUnits.setText(String.format("%s", "Miles"));
                miles = units * kiloConversion;
                convertedUnitsOutput.setText(String.format("%s", miles));
                break;
            case 2:
                unitsDisplay.setText(String.format("%s", "Inches"));
                unitsInput.setHint("Enter Inches here");
                convertedUnits.setText(String.format("%s", "Centimeters"));
                centimeters = units * inchesConversion;
                convertedUnitsOutput.setText(String.format("%s", centimeters));
                break;
            case 3:
                unitsDisplay.setText(String.format("%s", "Centimeters"));
                unitsInput.setHint("Enter Centimeters here");
                convertedUnits.setText(String.format("%s", "Inches"));
                inches = units * centiConversion;
                convertedUnitsOutput.setText(String.format("%s", inches));
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        spinnerPosition = position;
        convertAndDisplay(spinnerPosition);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            units = Double.parseDouble(unitsInput.getText().toString());
            storedUnits = Double.doubleToLongBits(units);
            convertAndDisplay(spinnerPosition);
        }
        return false;
    }

    @Override
    public void onPause() {

        Editor editor = savedValues.edit();
        editor.putLong("storedUnits", storedUnits);
        editor.apply();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

       storedUnits = savedValues.getLong("storedUnits", 0L);
       units = Double.longBitsToDouble(storedUnits);
       convertAndDisplay(spinnerPosition);
    }
}
