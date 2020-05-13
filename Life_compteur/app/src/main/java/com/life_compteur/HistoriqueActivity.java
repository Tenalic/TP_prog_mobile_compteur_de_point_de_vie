package com.life_compteur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoriqueActivity extends AppCompatActivity {

    private LinearLayout layout;
    private ImageView back;
    private ArrayList<Integer> listeDeleteOperation;
    private Integer lifePointP1;
    private Integer lifePointP2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique);

        back = findViewById(R.id.back);
        layout = (LinearLayout) findViewById(R.id.lifeZone);
        listeDeleteOperation = new ArrayList<Integer>();

        Intent intent = getIntent();

        /**
         * Recupère les donnée passé en entré
         */
        if (intent != null) {
            int lifeStart = intent.getIntExtra(getResources().getString(R.string.listeCoup), 8000);
            lifePointP1 = lifeStart;
            lifePointP2 = lifeStart;
            if (intent.hasExtra(getResources().getString(R.string.listeCoup))) {
                ArrayList<Coup> listeCoup = intent.getParcelableArrayListExtra(getResources().getString(R.string.listeCoup));
                if (listeCoup != null) {
                    TextView text = null;
                    Integer id = 0;
                    for (Coup coup : listeCoup) {
                        createLifeZoneText(coup, id);
                        id++;
                    }
                }
            }
        }


        /**
         * retourne vers MainActivity et retourne la liste des coup a annueler si non vide
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                if (listeDeleteOperation.isEmpty()) {
                    setResult(RESULT_CANCELED);
                } else {
                    result.putExtra("listeDeleteOperation", listeDeleteOperation);
                    setResult(RESULT_OK, result);
                }
                //setContentView(R.layout.life_counter);
                finish();
            }
        });
    }

    /**
     * Creer et ajoute a la vue un coup
     *
     * @param coup : coup que l'on souhaite afficher
     * @param id   : id de la textZone qui sera creer
     */
    protected void createLifeZoneText(Coup coup, final int id) {
        lifePointP1 = lifePointP1 + id;//coup.getLifeModificationPlayer1() * whichSigne(coup.getSigneModificationPlayer1());
        //lifePointP2 = lifePointP2 + coup.getLifeModificationPlayer2() * whichSigne(coup.getSigneModificationPlayer2());
        TextView text = new TextView(HistoriqueActivity.this);
        text.setText(Integer.toString(id));
        text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        text.setText(lifePointP1 + " " + ("+".equals(coup.getSigneModificationPlayer1()) ? "+" : "-") + coup.getLifeModificationPlayer1() + "    |   " + lifePointP2 + " " + ("+".equals(coup.getSigneModificationPlayer2()) ? "+" : "-") + coup.getLifeModificationPlayer2());
        text.setTextSize(40);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        if (coup.getLifeModificationPlayer1() != 0) {
            text.setTextColor(getResources().getColor(R.color.colorBlue));
        } else {
            text.setTextColor(getResources().getColor(R.color.colorRed));
        }
        text.setId(id);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean present = false;
                for (int id : listeDeleteOperation) {
                    if (id == v.getId()) {
                        present = true;
                    }
                }
                if (present != true) {
                    Toast.makeText(getApplicationContext(), "Click on " + v.getId(), Toast.LENGTH_SHORT).show();
                    listeDeleteOperation.add(v.getId());
                }
            }
        });
        layout.addView(text);
    }

    /**
     * Renvoie un entier correspondant au signe du symbole donner en entrée
     *
     * @param signe : + ou - attentdue
     * @return 1 si + sinon -1
     */
    protected int whichSigne(String signe) {
        return ("+".equals(signe) ? 1 : -1);
    }
}
