package com.example.thequiz;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    private Spinner spinnerMoeilijkheid;
    private Spinner spinnerCategorie;

    private TextView textViewHoogsteScore;

    private int hoogsteScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMoeilijkheid = findViewById(R.id.spinner_moeilijkheid);
        spinnerCategorie = findViewById(R.id.spinner_categorie);
        textViewHoogsteScore = findViewById(R.id.text_view_hoogste_score);

        laadCategorieen();
        laadMoeilijkheden();
        laadHoogsteScore();

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        Button buttonInstelling = findViewById(R.id.button_instelling_quiz);
        buttonInstelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInstelling();
            }
        });
    }

    //Start quiz with right category and diff
    private void startQuiz() {
        String moeilijkheid = spinnerMoeilijkheid.getSelectedItem().toString();
        Categorie selectCategorie = (Categorie) spinnerCategorie.getSelectedItem();
        int categorieID = selectCategorie.getId();
        String categorieNaam = selectCategorie.getName();

        Intent intent = new Intent(MainActivity.this, Quiz.class);
        intent.putExtra(EXTRA_DIFFICULTY, moeilijkheid);
        intent.putExtra(EXTRA_CATEGORY_ID, categorieID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categorieNaam);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    private void startInstelling() {
        Intent intent2 = new Intent(MainActivity.this, Instelling.class);
        startActivity(intent2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Gets score from end quiz
        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(Quiz.EXTRA_SCORE, 0);
                if (score > hoogsteScore) {
                    updateHoogsteScore(score);
                }
            }
        }
    }

    //Load categories into spinner
    private void laadCategorieen() {
        DbHelper dbHelper = DbHelper.getInstance(this);
        List<Categorie> categories = dbHelper.getAllCategories();

        ArrayAdapter<Categorie> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapterCategories);
    }

    //Load diff into spinner
    private void laadMoeilijkheden() {
        String[] Moeilijkheden = Vragen.getAllMoeilijkheden();

        ArrayAdapter<String> adapterMoeilijkheid = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Moeilijkheden);
        adapterMoeilijkheid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMoeilijkheid.setAdapter(adapterMoeilijkheid);
    }

    //load highscore
    private void laadHoogsteScore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        hoogsteScore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHoogsteScore.setText("Hoogste score: " + hoogsteScore);
    }

    //update highscore on mainactivity
    private void updateHoogsteScore(int hoogsteScoreNew) {
        hoogsteScore = hoogsteScoreNew;
        textViewHoogsteScore.setText("Hoogste score: " + hoogsteScore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, hoogsteScore);
        editor.apply();
    }
}
