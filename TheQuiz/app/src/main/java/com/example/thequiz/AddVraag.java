package com.example.thequiz;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddVraag extends AppCompatActivity {
    private TextView newVraag;
    private TextView optie1;
    private TextView optie2;
    private TextView optie3;
    private TextView antwnr;

    private Spinner spinnerMoeilijkheid;
    private Spinner spinnerCategorie;

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vraag);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DbHelper.instance = new DbHelper(this);

        spinnerMoeilijkheid = findViewById(R.id.spinner_moeilijkheid);
        spinnerCategorie = findViewById(R.id.spinner_categorie);
        newVraag = findViewById(R.id.edittext_vraag);
        optie1 = findViewById(R.id.edittext_optie1);
        optie2 = findViewById(R.id.edittext_optie2);
        optie3 = findViewById(R.id.edittext_optie3);
        antwnr = findViewById(R.id.edittext_nr);
        addButton = findViewById(R.id.button_add);

        laadCategorie();
        laadMoeilijkheid();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newVraag.getText().toString().trim().length() == 0 | antwnr.getText().toString().trim().length() == 0) {
                    Toast.makeText(getBaseContext(),"Vul vraagveld in.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (optie1.getText().toString().trim().length() == 0 | optie2.getText().toString().trim().length() == 0 |
                        optie3.getText().toString().trim().length() == 0) {
                    Toast.makeText(getBaseContext(),"Vul alle optiesvelden in.",Toast.LENGTH_SHORT).show();
                    return;
                }

                String new_vraag = newVraag.getText().toString();
                String optie_1 = optie1.getText().toString();
                String optie_2 = optie2.getText().toString();
                String optie_3 = optie3.getText().toString();
                String antw_nr = antwnr.getText().toString();
                int value = Integer.parseInt(antw_nr);
                String moeilijkheid = spinnerMoeilijkheid.getSelectedItem().toString();
                Categorie selectCategorie = (Categorie) spinnerCategorie.getSelectedItem();
                int categorieID = selectCategorie.getId();

                if(value < 1 | value > 3){
                    Toast.makeText(getBaseContext(),"Vul een geldig optienummer in kies uit 1 2 of 3.",Toast.LENGTH_SHORT).show();
                    return;
                }

                Vragen q1 = new Vragen(new_vraag, optie_1, optie_2, optie_3, value,
                        moeilijkheid, categorieID);
                DbHelper.instance.addVraag(q1);

                ((EditText) findViewById(R.id.edittext_vraag)).getText().clear();
                ((EditText) findViewById(R.id.edittext_optie1)).getText().clear();
                ((EditText) findViewById(R.id.edittext_optie2)).getText().clear();
                ((EditText) findViewById(R.id.edittext_optie3)).getText().clear();
                ((EditText) findViewById(R.id.edittext_nr)).getText().clear();

                Toast.makeText(getBaseContext(),"Nieuwe vraag is opgeslagen",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void laadCategorie() {
        DbHelper dbHelper = DbHelper.getInstance(this);
        List<Categorie> categories = dbHelper.getAllCategories();

        ArrayAdapter<Categorie> adapterCategorie = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapterCategorie);
    }

    private void laadMoeilijkheid() {
        String[] moeilijkheidsgraad = Vragen.getAllMoeilijkheden();

        ArrayAdapter<String> adapterMoeilijkheid = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, moeilijkheidsgraad);
        adapterMoeilijkheid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMoeilijkheid.setAdapter(adapterMoeilijkheid);
    }
}
