package com.life_compteur;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;

public class Setting extends Dialog {

    private int lifeStart;
    private int timerMinutes;
    private int timerSecondes;
    private Button buttofinish;
    private EditText lifeStartInput;
    private EditText timerMinutesInput;
    private EditText timerSecondesInput;

    public Setting(Activity activity) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.setting);

        this.lifeStartInput = findViewById(R.id.lifestartinput);
        this.timerMinutesInput = findViewById(R.id.timerminutesinput);
        this.timerSecondesInput = findViewById(R.id.timersecondesinput);
        this.buttofinish = findViewById(R.id.buttonfinish);
    }

    public void build(int lifeStart , int timerMinutes, int timerSecondes) {
        this.lifeStart = lifeStart;
        this.timerMinutes = timerMinutes;
        this.timerSecondes = timerSecondes;
        this.lifeStartInput.setText(Integer.toString(this.lifeStart));
        this.timerMinutesInput.setText(Integer.toString(this.timerMinutes));
        this.timerSecondesInput.setText(Integer.toString(this.timerSecondes));
        show();
    }

    public int getLifeStart() {
        return lifeStart;
    }

    public void setLifeStart(int lifeStart) {
        this.lifeStart = lifeStart;
    }

    public int getTimerMinutes() {
        return timerMinutes;
    }

    public void setTimerMinutes(int timerMinutes) {
        this.timerMinutes = timerMinutes;
    }

    public int getTimerSecondes() {
        return timerSecondes;
    }

    public void setTimerSecondes(int timerSecondes) {
        this.timerSecondes = timerSecondes;
    }

    public Button getButtofinish() {
        return buttofinish;
    }
    

    public EditText getLifeStartInput() {
        return lifeStartInput;
    }


    public EditText getTimerMinutesInput() {
        return timerMinutesInput;
    }

    public EditText getTimerSecondesInput() {
        return timerSecondesInput;
    }

}
