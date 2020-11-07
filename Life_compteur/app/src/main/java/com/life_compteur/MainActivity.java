package com.life_compteur;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int lifeStart;
    private TextView lifePlayer1;
    private Integer lifePointPlayer1;
    private TextView lifePlayer2;
    private Integer lifePointPlayer2;
    private MainActivity activity;
    private String stringNumber;
    private Integer intNumber;
    private ArrayList<Coup> listeCoup;
    private ImageView historique;

    // Chrono
    private MediaPlayer mediaPlayer;

    private Chronometer mChronometer;

    private boolean marche = false;
    private int min;
    private int sec;
    private long lastPause;
    private int tmp; // eviter 2 popup lors de la sonnerie
    // Fin chrono

    private ImageView imageViewDice;
    private Random rng = new Random();


    // Clée de sauvegarde
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PARAMETRE_LP1 = "LP1";
    public static final String PARAMETRE_LP2 = "LP2";
    public static final String PARAMETRE_MIN = "MIN";
    public static final String PARAMETRE_SEC = "SEC";
    public static final String PARAMETRE_STARTLIFE = "SLP";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.life_counter);

        this.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);

        this.listeCoup = new ArrayList<Coup>();
        this.lifePlayer1 = findViewById(R.id.player1);
        this.lifePlayer2 = findViewById(R.id.player2);
        this.activity = this;
        this.historique = findViewById(R.id.historique);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViewDice = findViewById(R.id.image_view_dice);
        imageViewDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });

        loadStartParam();

        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        mChronometer.setCountDown(true);
        mChronometer.setBase((SystemClock.elapsedRealtime() + min * 60000 + sec * 1000));

        mChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marche) {
                    marche = true;
                    if (lastPause != 0) {
                        mChronometer.setBase(mChronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                    } else {
                        mChronometer.setBase(SystemClock.elapsedRealtime() + (min * 60000 + sec * 1000));
                    }
                    mChronometer.start();
                } else {
                    marche = false;
                    lastPause = SystemClock.elapsedRealtime();
                    mChronometer.stop();
                }

            }
        });


        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsed = SystemClock.elapsedRealtime() - mChronometer.getBase();
                int min2 = (int) (elapsed / 60000) * (-1);
                int sec2 = (int) ((elapsed / 1000) % 60) * (-1);
                if ((min2 == 0) && (sec2 == 0) && tmp != 1) {
                    tmp++;
                    AlertDialog.Builder AlarmPopUp = new AlertDialog.Builder(activity);
                    AlarmPopUp.setTitle("Alarme");
                    AlarmPopUp.setMessage("Limite de temps atteint");
                    AlarmPopUp.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mediaPlayer.stop();
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);
                            Toast.makeText(getApplicationContext(), "Vous avez éteint l'alarme.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlarmPopUp.show();
                    mediaPlayer.start();
                }
            }
        });

        loadLifePoint();

        lifePlayer1.setText(lifePointPlayer1.toString());
        lifePlayer2.setText(lifePointPlayer2.toString());

        stringNumber = "";
        intNumber = 0;

        lifePlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calculator calculator = new Calculator(activity);
                calculator.build("Player 1");
                /**
                 * Event bouton +
                 * Ajoute la valeur saisie au point de vie du joueur 1
                 */
                calculator.getPlusButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            stringNumber = calculator.getInputNumber().getText().toString();
                            intNumber = Integer.parseInt(stringNumber);
                            lifePointPlayer1 = lifePointPlayer1 + intNumber;
                            listeCoup.add(new Coup(lifePointPlayer1, "+", intNumber, lifePointPlayer2, null, 0));
                            lifePlayer1.setText(lifePointPlayer1.toString());
                            Toast.makeText(getApplicationContext(), "+" + stringNumber, Toast.LENGTH_SHORT).show();
                            saveLifePoint();
                            calculator.getInputNumber().setText("");
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /**
                 * Event bouton -
                 * Retire la valeur saisie au point de vie du joueur 1
                 */
                calculator.getMoinsButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            stringNumber = calculator.getInputNumber().getText().toString();
                            intNumber = Integer.parseInt(stringNumber);
                            lifePointPlayer1 = lifePointPlayer1 - intNumber;
                            listeCoup.add(new Coup(lifePointPlayer1, "-", intNumber, lifePointPlayer2, null, 0));
                            lifePlayer1.setText(lifePointPlayer1.toString());
                            Toast.makeText(getApplicationContext(), "-" + stringNumber, Toast.LENGTH_SHORT).show();
                            saveLifePoint();
                            calculator.getInputNumber().setText("");
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                calculator.getDivButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            intNumber = lifePointPlayer1 / 2;
                            listeCoup.add(new Coup(lifePointPlayer1, "-", intNumber, lifePointPlayer2, null, 0));
                            lifePointPlayer1 = lifePointPlayer1 - intNumber;
                            lifePlayer1.setText(lifePointPlayer1.toString());
                            Toast.makeText(getApplicationContext(), "-" + intNumber, Toast.LENGTH_SHORT).show();
                            saveLifePoint();
                        } catch (Error e) {

                        }
                    }
                });

                calculator.getBackButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calculator.dismiss();
                    }
                });

            }
        });

        lifePlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calculator calculator = new Calculator(activity);
                calculator.build("Player 2");
                /**
                 * Event bouton +
                 * Ajoute la valeur saisie au point de vie du joueur 2
                 */
                calculator.getPlusButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            stringNumber = calculator.getInputNumber().getText().toString();
                            intNumber = Integer.parseInt(stringNumber);
                            lifePointPlayer2 = lifePointPlayer2 + intNumber;
                            listeCoup.add(new Coup(lifePointPlayer1, null, 0, lifePointPlayer2, "+", intNumber));
                            lifePlayer2.setText(lifePointPlayer2.toString());
                            Toast.makeText(getApplicationContext(), "+" + stringNumber, Toast.LENGTH_SHORT).show();
                            saveLifePoint();
                            calculator.getInputNumber().setText("");
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /**
                 * Event bouton -
                 * Retire la valeur saisie au point de vie du joueur 2
                 */
                calculator.getMoinsButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            stringNumber = calculator.getInputNumber().getText().toString();
                            intNumber = Integer.parseInt(stringNumber);
                            lifePointPlayer2 = lifePointPlayer2 - intNumber;
                            listeCoup.add(new Coup(lifePointPlayer1, null, 0, lifePointPlayer2, "-", intNumber));
                            lifePlayer2.setText(lifePointPlayer2.toString());
                            Toast.makeText(getApplicationContext(), "-" + stringNumber, Toast.LENGTH_SHORT).show();
                            saveLifePoint();
                            calculator.getInputNumber().setText("");
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                calculator.getDivButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            intNumber = lifePointPlayer2 / 2;
                            listeCoup.add(new Coup(lifePointPlayer1, null, 0, lifePointPlayer2, "-", intNumber));
                            lifePointPlayer2 = lifePointPlayer2 - intNumber;
                            lifePlayer2.setText(lifePointPlayer2.toString());
                            Toast.makeText(getApplicationContext(), "-" + intNumber, Toast.LENGTH_SHORT).show();
                            saveLifePoint();
                        } catch (Error e) {

                        }
                    }
                });

                /**
                 * fermer la popup
                 */
                calculator.getBackButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calculator.dismiss();
                    }
                });
            }
        });

        /**
         * Creer l'activiter historique et l'ouvre
         */
        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historiqueActivity = new Intent(getApplicationContext(), HistoriqueActivity.class);
                historiqueActivity.putExtra(getResources().getString(R.string.listeCoup), listeCoup);
                historiqueActivity.putExtra(getResources().getString(R.string.lifestart), lifeStart);
                startActivityForResult(historiqueActivity, 1);
            }
        });
    }

    /**
     * Creer le menu lors de la création de l'activité
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Est appeler quand une option du menu est cliquer
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            /**
             * Remet le timer a ces valeur d'origin
             */
            case R.id.resettimer:
                loadTimer();
                marche = false;
                mChronometer.stop();
                mChronometer.setBase(SystemClock.elapsedRealtime() + (min * 60000 + sec * 1000));
                lastPause = 0;
                tmp = 0;
                return true;
            /**
             * Remet les points de vie a leurs valeur d'origin
             */
            case R.id.resetlp:
                loadStartLifePoint();
                lifePointPlayer1 = lifeStart;
                lifePointPlayer2 = lifeStart;
                lifePlayer1.setText(lifePointPlayer1.toString());
                lifePlayer2.setText(lifePointPlayer2.toString());
                saveLifePoint();
                this.listeCoup = new ArrayList<Coup>();
                return true;

            /**
             * Affiche la popup pour modifier les valeur d'origin
             */
            case R.id.setting:
                final Setting setting = new Setting(activity);
                setting.build(lifeStart, min, sec);
                setting.getButtofinish().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            saveTimerParam(Integer.parseInt(setting.getTimerMinutesInput().getText().toString()), Integer.parseInt(setting.getTimerSecondesInput().getText().toString()));
                            saveStartLifeParam(Integer.parseInt(setting.getLifeStartInput().getText().toString()));
                            setting.dismiss();
                        } catch (Error e) {

                        }

                    }
                });
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * est appeler lorsque que l'on viens de l'activité historique
     * car elle nous renvoie des donnée
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * Supprime les coup dans listeCoup et met a jour l'affichage des point de vie
         */
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ArrayList<Integer> listeDeleteOperation = data.getIntegerArrayListExtra("listeDeleteOperation");
                    Coup coup = null;
                    int correction = 0;
                    for (int id : listeDeleteOperation) {
                        if (id - correction < 0) {
                            coup = listeCoup.get(id);
                            if (coup != null) {
                                this.lifePointPlayer1 = lifePointPlayer1 + coup.getLifeModificationPlayer1() * ("+".equals(coup.getSigneModificationPlayer1()) ? -1 : 1);
                                this.lifePointPlayer2 = lifePointPlayer2 + coup.getLifeModificationPlayer2() * ("+".equals(coup.getSigneModificationPlayer2()) ? -1 : 1);
                                listeCoup.remove(id);
                                correction++;
                            }
                        } else {
                            coup = listeCoup.get(id - correction);
                            if (coup != null) {
                                this.lifePointPlayer1 = lifePointPlayer1 + coup.getLifeModificationPlayer1() * ("+".equals(coup.getSigneModificationPlayer1()) ? -1 : 1);
                                this.lifePointPlayer2 = lifePointPlayer2 + coup.getLifeModificationPlayer2() * ("+".equals(coup.getSigneModificationPlayer2()) ? -1 : 1);
                                listeCoup.remove(id - correction);
                                correction++;
                            }
                        }

                    }
                    this.lifePlayer1.setText(this.lifePointPlayer1.toString());
                    this.lifePlayer2.setText(this.lifePointPlayer2.toString());
                }
            }
        }
    }

    /**
     * Sauvegarde les point de vie du joueur 1 et 2
     */
    public void saveLifePoint() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(PARAMETRE_LP1, lifePointPlayer1);
        editor.putInt(PARAMETRE_LP2, lifePointPlayer2);

        editor.commit();
    }

    /**
     * Sauvegarde les valeur d'origin du timer
     *
     * @param min = minutes
     * @param sec = secondes
     */
    public void saveTimerParam(final int min, final int sec) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(PARAMETRE_MIN, min);
        editor.putInt(PARAMETRE_SEC, sec);

        editor.commit();
    }

    /**
     * Sauvegarde la valeur d'origine des points de vie
     *
     * @param startLife = valeur de depart des points de vie
     */
    public void saveStartLifeParam(final int startLife) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(PARAMETRE_STARTLIFE, startLife);

        editor.commit();
    }

    /**
     * charge les donnée des points de vie en mémoire
     */
    public void loadLifePoint() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lifePointPlayer1 = sharedPreferences.getInt(PARAMETRE_LP1, lifeStart);
        lifePointPlayer2 = sharedPreferences.getInt(PARAMETRE_LP2, lifeStart);
    }

    /**
     * Charge les valeur du timer ainsi que la valeur des points de vie de depart
     */
    public void loadStartParam() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lifeStart = sharedPreferences.getInt(PARAMETRE_STARTLIFE, 8000);
        min = sharedPreferences.getInt(PARAMETRE_MIN, 40);
        sec = sharedPreferences.getInt(PARAMETRE_SEC, 0);
    }

    /**
     * charge les donnée d'origin du timer
     */
    public void loadTimer() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        min = sharedPreferences.getInt(PARAMETRE_MIN, 40);
        sec = sharedPreferences.getInt(PARAMETRE_SEC, 0);
    }

    /**
     * Charge les donnée d'origin des points de vie
     */
    public void loadStartLifePoint() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lifeStart = sharedPreferences.getInt(PARAMETRE_STARTLIFE, 8000);
    }

    /**
     * Genere un nombre entre 1 et 6 et affiche l'image du dé correspondant
     */
    private void rollDice() {
        int randomNumber = rng.nextInt(6) + 1;
        switch (randomNumber) {
            case 1:
                imageViewDice.setImageResource(R.drawable.dice1);
                break;
            case 2:
                imageViewDice.setImageResource(R.drawable.dice2);
                break;
            case 3:
                imageViewDice.setImageResource(R.drawable.dice3);
                break;
            case 4:
                imageViewDice.setImageResource(R.drawable.dice4);
                break;
            case 5:
                imageViewDice.setImageResource(R.drawable.dice5);
                break;
            case 6:
                imageViewDice.setImageResource(R.drawable.dice6);
                break;
        }
        Toast.makeText(getApplicationContext(), "You roll a " + randomNumber, Toast.LENGTH_SHORT).show();
    }
}
