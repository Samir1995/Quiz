package com.example.thequiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Quiz extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private TextView textViewVraag;
    private TextView textViewScore;
    private TextView textViewVraagCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroep;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button btnBevestigNext;
    private TextView textViewMoeilijkheid;
    private TextView textViewCategorie;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private List<Vragen> vraagList;

    private int vraagCounter;
    private int vraagCountTotal;
    private Vragen huidigVraag;

    private int score;
    private boolean antwoord;

    private long backButtonTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewVraag = findViewById(R.id.text_view_vraag);
        textViewScore = findViewById(R.id.text_view_score);
        textViewVraagCount = findViewById(R.id.text_view_vraag_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroep = findViewById(R.id.radio_groep);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        btnBevestigNext = findViewById(R.id.button_bevestig_next);
        textViewMoeilijkheid = findViewById(R.id.text_view_moeilijkheid);
        textViewCategorie = findViewById(R.id.text_view_categorie);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        Intent intent = getIntent();
        int categorieID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
        String moeilijkheid = intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);

        textViewCategorie.setText("Categorie: " + categoryName);
        textViewMoeilijkheid.setText("Moeilijkheidsgraad: " + moeilijkheid);

        DbHelper dbHelper = DbHelper.getInstance(this);
        vraagList = dbHelper.getAllVragen();
        vraagList = dbHelper.getVraag(categorieID, moeilijkheid);
        vraagCountTotal = vraagList.size();
        Collections.shuffle(vraagList);

        showNextVraag();

        btnBevestigNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!antwoord) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAntwoord();
                    } else {
                        Toast.makeText(Quiz.this, "Selecteer een Antwoord", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextVraag();
                }
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

    // Gets next answer and shows it
    private void showNextVraag() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroep.clearCheck();

        if (vraagCounter < vraagCountTotal) {
            huidigVraag = vraagList.get(vraagCounter);

            textViewVraag.setText(huidigVraag.getVraag());
            rb1.setText(huidigVraag.getOptie1());
            rb2.setText(huidigVraag.getOptie2());
            rb3.setText(huidigVraag.getOptie3());

            vraagCounter++;
            textViewVraagCount.setText("Vraag: " + vraagCounter + "/" + vraagCountTotal);
            antwoord = false;
            btnBevestigNext.setText("Bevestig");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAntwoord();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    //Check if answer is correct and change score
    private void checkAntwoord() {
        antwoord = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroep.getCheckedRadioButtonId());
        int answerNr = rbGroep.indexOfChild(rbSelected) + 1;

        if (answerNr == huidigVraag.getAntwoordNr()) {
            score++;
            textViewScore.setText("Score: " + score);
        }

        showAntwoord();
    }

    //Shows correct answer
    private void showAntwoord() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (huidigVraag.getAntwoordNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewVraag.setText("Antwoord 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewVraag.setText("Antwoord 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewVraag.setText("Antwoord 3 is correct");
                break;
        }

        if (vraagCounter < vraagCountTotal) {
            btnBevestigNext.setText(" Volgende vraag ");
        } else {
            btnBevestigNext.setText(" Quiz Afronden ");
        }
    }

    //Stops quiz
    private void finishQuiz() {
        //passes score to MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backButtonTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Klik nog een keer om de quiz te beëindigen", Toast.LENGTH_SHORT).show();
        }

        backButtonTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
