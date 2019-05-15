package com.saveandstudio.mario.cdd.GameBasic;

import android.view.MotionEvent;

public class Input {
    static public Vector2 touchPosition = new Vector2();
    static public MotionEvent event;
    static public boolean touching = false;
    static public boolean touchDown = false;
    static public boolean touchUp = false;
}
