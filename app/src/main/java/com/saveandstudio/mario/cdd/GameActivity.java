package com.saveandstudio.mario.cdd;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.saveandstudio.mario.cdd.Components.CardSystem;
import com.saveandstudio.mario.cdd.GameBasic.Input;
import com.saveandstudio.mario.cdd.GameBasic.Physics;
import com.saveandstudio.mario.cdd.GameBasic.Renderer;
import com.saveandstudio.mario.cdd.Scenes.Scene;

import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    private Toast newGame_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();

        Button mainMenu = findViewById(R.id.main_menu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                exit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        exit();
    }


    public void exit() {
        if (Renderer.renderersList != null) {
            Renderer.renderersList.clear();
        }
        Scene.Clear();
        Physics.Clear();
        CardSystem.getInstance().remove();
    }

    private void hideNavigationBar() {
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
