package com.example.alexandremguay.DDAfinalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static com.example.alexandremguay.DDAfinalproject.HomeworkTracker.textAsBitmap;

/**
 * Created by AMG on 2018-01-02.
 */

public class MainActivity extends AppCompatActivity {
    protected FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("D.D.A");
        fab = findViewById(R.id.fab);
        fab.setImageBitmap(textAsBitmap("ÐÐÅ", 250, Color.BLACK));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snacker("Welcome to our App!");
            }
        });
    }

    public void snacker(String msgs) {
        Snackbar snack = Snackbar.make(this.findViewById(R.id.fab), msgs, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextSize(32);
        snack.show();
    }
 //   https://stackoverflow.com/questions/32668217/android-snackbar-textalignment-in-center    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.recorder:
                Intent rec = new Intent(MainActivity.this, MemoRecorderActivity.class);
                startActivity(rec);
                break;

            case R.id.homework:
                Intent homewk = new Intent(MainActivity.this, HomeworkTracker.class);
                startActivity(homewk);
                break;

            case R.id.gastracker:
                Intent gastrack = new Intent(MainActivity.this, GasTrackerActivity.class);
                startActivity(gastrack);
                break;

            case R.id.quit:
                finish();
                System.exit(0);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
