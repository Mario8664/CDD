package com.saveandstudio.mario.cdd;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.Scenes.Scene;

import java.util.Collections;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    //用于控制SurfaceView
    private SurfaceHolder surfaceHolder;
    //声明一个画笔
    private Paint paint;
    //声明一条线程
    private GameThread thread;
    //线程消亡的标识位
    private boolean flag = false;
    private int touchX, touchY;
    private boolean touching;
    private boolean touchDown = false;
    private boolean touchUp = true;
    //声明一个画布
    private Canvas canvas;
    //声明屏幕的宽高
    private int screenW, screenH;
    //surfaceView的宽高比
    private int viewW = 9, viewH = 16;
    private long frameDeltaTime = 16;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MySurfaceView(Context context, @Nullable AttributeSet attributeSet, int defferentStyleAttribute) {
        super(context, attributeSet, defferentStyleAttribute);
        Global.surfaceContext = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
        paint = new Paint();

        paint.setColor(getResources().getColor(R.color.colorPrimary));
        Scene.getInstance().prePareScene();
        //设置焦点
        setFocusable(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        screenW = getWidth();
        screenH = (int) ((float) screenW / (float) viewW * (float) viewH);
        surfaceHolder.setFixedSize(screenW, screenH);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenW = this.getWidth();
        screenH = this.getHeight();
        flag = true;
        setZOrderOnTop(false);
        setZOrderMediaOverlay(false);

        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    //绘图
    private void render(Canvas canvas) {
        try{
            paint.setColor(getResources().getColor(R.color.colorPrimary));
            canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
            synchronized (Renderer.renderersList){
                if (Renderer.renderersList != null && !Renderer.clear) {
                    //sort
                    Collections.sort(Renderer.renderersList);
                    //render
                    for (int i = 0; i < Renderer.renderersList.size(); i++) {
                        Renderer.renderersList.get(i).Draw(canvas, paint);
                    }
                }

            }

        }
        catch (NullPointerException e){

        }
    }

    //触屏事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = (int) event.getX();
        touchY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                touching = true;
                return true;
            case MotionEvent.ACTION_DOWN:
                touchDown = true;
                touching = true;
                return true;
            case MotionEvent.ACTION_UP:
                touchUp = true;
                touching = false;
                return true;
            default:

        }
        return true;
    }


    //逻辑
    private void logic() {
        //Update screen info
        GameViewInfo.screenW = screenW;
        GameViewInfo.screenH = screenH;
        updateInput();
        //Update
        synchronized (Scene.getInstance()){
            if (Scene.getInstance().gameObjectsList != null) {
                for (int i = 0; i < Scene.getInstance().gameObjectsList.size(); i++) {
                    Scene.getInstance().gameObjectsList.get(i).Update();
                }
            }
            Scene.getInstance().Clear();

        }
    }

    private void updateInput(){
        //Update input
        if (Input.touchPosition != null) {
            Input.touchPosition = new Vector2((float) touchX / GameViewInfo.screenW * GameViewInfo.fixedW,
                    (float) touchY / GameViewInfo.screenH * GameViewInfo.fixedH);
        }
            Input.touching = touching;
            Input.touchDown = false;
            Input.touchUp = false;
            if(touchDown){
                Input.touchDown = true;
                touchDown = false;
            }
            if(touchUp){
                Input.touchUp = true;
                touchUp = false;
            }

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
        flag = false;
        Global.surfaceContext = null;
    }

    class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private MySurfaceView gameView;
        private boolean run = false;private final Object lock = new Object();
        private boolean pause = false;

        public GameThread(SurfaceHolder surfaceHolder, MySurfaceView gameView) {
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;
        }

        public void setRunning(boolean run) {
            this.run = run;
        }

        public SurfaceHolder getSurfaceHolder() {
            return surfaceHolder;
        }

        @Override
        public void run() {
            while (run) {
                    logic();
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas != null) {
                        synchronized (surfaceHolder) {
                            //call methods to draw and process next fame
                            gameView.render(canvas);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                long start = System.currentTimeMillis();
                long end = System.currentTimeMillis();
                try {
                    if (end - start < frameDeltaTime) {
                        Thread.sleep(frameDeltaTime - (end - start));
                        GameViewInfo.deltaTime = (float) frameDeltaTime / 1000;
                    } else {
                        GameViewInfo.deltaTime = (float) (end - start) / 1000;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


