package com.saveandstudio.mario.cdd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    //用于控制SurfaceView
    private SurfaceHolder surfaceHolder;
    //声明一个画笔
    private Paint paint;
    //触摸的坐标
    private int touchX = 10, touchY = 10;
    //声明一条线程
    private GameThread thread;
    //线程消亡的标识位
    private boolean flag;
    //声明一个画布
    private Canvas canvas;
    //声明屏幕的宽高
    private int screenW, screenH;
    private long frameDeltaTime;


    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MySurfaceView(Context context, @Nullable AttributeSet attributeSet, int defferentStyleAttribute) {
        super(context, attributeSet, defferentStyleAttribute);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        paint = new Paint();

        paint.setColor(getResources().getColor(R.color.colorPrimary));
        //设置焦点
        setFocusable(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenW = this.getWidth();
        screenH = this.getHeight();
        flag = true;
        //实例线程
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        //启动线程
        thread.start();
    }

    //绘图
    public void render(Canvas canvas) {
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawText("Touch", touchX, touchY, paint);
    }

    //触屏事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = (int) event.getX();
        touchY = (int) event.getY();
        return true;
    }

    //逻辑
    private void logic() {
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
    }

    class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private MySurfaceView gameView;
        private boolean run = false;

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
                logic();
                long start = System.currentTimeMillis();
                long end = System.currentTimeMillis();
                try {
                    if (end - start < frameDeltaTime) {
                        Thread.sleep(frameDeltaTime - (end - start));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


