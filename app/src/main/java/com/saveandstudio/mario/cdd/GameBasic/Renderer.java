package com.saveandstudio.mario.cdd.GameBasic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import com.saveandstudio.mario.cdd.R;

import java.util.ArrayList;

public class Renderer extends MonoBehavior implements Comparable<Renderer> {

    static public ArrayList<Renderer> renderersList = new ArrayList<>();
    public static boolean clear = false;

    protected Transform transform;
    private Bitmap bitmapResource;
    private int bitMapHeight, bitMapWidth;
    private int bitmapId;
    private int lastID;
    protected float zDepth;
    private boolean set = false;

    public Renderer() {
        this(R.mipmap.default_sprite);
    }

    public Renderer(int id) {
        bitmapId = id;
        clear = false;
        synchronized (renderersList){
            renderersList.add(this);
        }
    }

    public void setBitmapResource(int id) {
        bitmapId = id;
        set = true;
    }

    public Bitmap getBitmapResource() {
        return bitmapResource;
    }

    public void Draw(Canvas canvas, Paint paint) {

        if (transform == null) {
            transform = (Transform) getComponent(Transform.class);
        } else {
            if (bitmapResource == null) {
                if (Global.surfaceContext != null) {
                    bitmapResource = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), bitmapId);
                    lastID = bitmapId;
                    bitMapWidth = bitmapResource.getWidth();
                    bitMapHeight = bitmapResource.getHeight();
                }
            } else {
                canvas.drawBitmap(bitmapResource, transform.transformMatrix, null);
                if (set) {
                    if(bitmapId != lastID){
                        bitmapResource = BitmapFactory.decodeResource(Global.surfaceContext.getResources(), bitmapId);
                        bitMapWidth = bitmapResource.getWidth();
                        bitMapHeight = bitmapResource.getHeight();
                        lastID = bitmapId;
                    }
                    set = false;
                }

            }
            zDepth = transform.getPosition().z;
        }
        if (gameObject.toBeDestroy) {
            Destroy();
        }
    }

    @Override
    public int compareTo(@NonNull Renderer renderer) {
        if (zDepth > renderer.zDepth) {
            return 1;
        } else if (zDepth == renderer.zDepth) {
            return 0;
        } else {
            return -1;
        }
    }

    public void Destroy() {
        synchronized (renderersList){
            renderersList.remove(this);
        }

    }

    public int getBitMapWidth() {
        return bitMapWidth;
    }

    public int getBitMapHeight() {
        return bitMapHeight;
    }

}
