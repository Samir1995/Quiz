package com.example.thequiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.Spinner;




public class Instelling extends AppCompatActivity {
    private Button add;
    private Button del;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instelling);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add = findViewById(R.id.button_add);
        del = findViewById(R.id.button_del);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_vraag = new Intent(Instelling.this, AddVraag.class);
                startActivity(add_vraag);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent del_vraag = new Intent(Instelling.this, DelVraag.class);
                startActivity(del_vraag);
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

}