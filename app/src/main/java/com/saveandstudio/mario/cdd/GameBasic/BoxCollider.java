package com.saveandstudio.mario.cdd.GameBasic;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class BoxCollider extends MonoBehavior implements Comparable<BoxCollider> {
    private Vector2 size;
    private Vector2 offset;

    public BoxCollider() {
        this(Vector2.one, Vector2.zero);
    }

    public BoxCollider(Vector2 size, Vector2 offset) {
        this.size = size.clone();
        this.offset = offset.clone();
        if(Physics.colliders == null){
            Physics.colliders = new ArrayList<>();
        }
        Physics.colliders.add(this);
    }

    public void setSize(Vector2 size) {
        this.size = size.clone();
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset.clone();
    }

    public Vector2 getSize() {
        return new Vector2(size.x / GameViewInfo.screenW * GameViewInfo.fixedW,
                size.y / GameViewInfo.screenH * GameViewInfo.fixedH);
    }

    public Vector2 getOffset() {
        return offset;
    }

    @Override
    public int compareTo(@NonNull BoxCollider b) {
        float aZ = ((Transform)getComponent(Transform.class)).getPosition().z;
        float bZ = ((Transform)b.getComponent(Transform.class)).getPosition().z;
        if(aZ > bZ){
            return -1;
        }
        else if(aZ == bZ){
            return 0;
        }
        else
            return 1;
    }
}
