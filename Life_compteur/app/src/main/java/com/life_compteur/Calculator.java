package com.life_compteur;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Calculator extends Dialog {

    private String title;
    private Integer number;
    private Button plusButton;
    private Button moinsButton;
    private Button backButton;
    private TextView titleView;
    private EditText inputNumber;
    private Button divButton;

    public Calculator(Activity activity) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.calculateur);
        this.title = "Player X";
        this.number = 0;
        this.moinsButton = findViewById(R.id.buttonMoins);
        this.plusButton = findViewById(R.id.buttonPlus);
        this.titleView = findViewById(R.id.title);
        this.inputNumber = findViewById(R.id.numberInput);
        this.backButton = findViewById(R.id.back);
        this.divButton = findViewById(R.id.div2);
    }

    public void build(String title) {
        show();
        titleView.setText(title);
    }

    public Button getPlusButton() {
        return plusButton;
    }

    public String getTitle() {
        return title;
    }

    public Button getMoinsButton() {
        return moinsButton;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public Integer getNumber() {
        return number;
    }

    public Button getBackButton() {
        return backButton;
    }

    public EditText getInputNumber() {
        return inputNumber;
    }

    public Button getDivButton() {
        return divButton;
    }
}
