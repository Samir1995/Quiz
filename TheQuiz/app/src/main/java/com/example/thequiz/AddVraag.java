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
    Context context = this;

    private Spinner spinnerDifficulty;
    private Spinner spinnerCategory;

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vraag);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DbHelper.instance = new DbHelper(context);

        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerCategory = findViewById(R.id.spinner_category);
        newVraag = findViewById(R.id.edittext_vraag);
        optie1 = findViewById(R.id.edittext_optie1);
        optie2 = findViewById(R.id.edittext_optie2);
        optie3 = findViewById(R.id.edittext_optie3);
        antwnr = findViewById(R.id.edittext_nr);
        addButton = findViewById(R.id.button_del);

        loadCategories();
        loadDifficultyLevels();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newVraag.getText().toString().trim().length() == 0 | antwnr.getText().toString().trim().length() == 0) {
                    return;
                }

                String new_vraag = newVraag.getText().toString();
                String optie_1 = optie1.getText().toString();
                String optie_2 = optie2.getText().toString();
                String optie_3 = optie3.getText().toString();
                String antw_nr = antwnr.getText().toString();
                int value = Integer.parseInt(antw_nr);
                String difficulty = spinnerDifficulty.getSelectedItem().toString();
                Categorie selectedCategory = (Categorie) spinnerCategory.getSelectedItem();
                int categoryID = selectedCategory.getId();


                Vragen q1 = new Vragen(new_vraag, optie_1, optie_2, optie_3, value,
                        difficulty, categoryID);
                DbHelper.instance.addQuestion(q1);

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

    private void loadCategories() {
        DbHelper dbHelper = DbHelper.getInstance(this);
        List<Categorie> categories = dbHelper.getAllCategories();

        ArrayAdapter<Categorie> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }

    private void loadDifficultyLevels() {
        String[] difficultyLevels = Vragen.getAllDifficultyLevels();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }
}
