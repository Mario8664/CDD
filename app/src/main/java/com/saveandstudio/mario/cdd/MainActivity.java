package com.saveandstudio.mario.cdd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toast setting_hint;
    private Toast about_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button startGame = findViewById(R.id.start_game);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(MainActivity.this, GameActivity.class);
                startActivity(game);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        });

        Button exitGame = findViewById(R.id.exit_game);
        exitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                if (setting_hint != null) {
                    setting_hint.cancel();
                }
                setting_hint = Toast.makeText(MainActivity.this, "|SXT|WWJ|PXX|LYJ|", Toast.LENGTH_SHORT);
                setting_hint.show();
                break;
            case R.id.about:
                if (about_hint != null) {
                    about_hint.cancel();
                }
                about_hint = Toast.makeText(MainActivity.this, "Save& Studio 出品", Toast.LENGTH_SHORT);
                about_hint.show();
                break;
            default:

        }
        return true;
    }
}
