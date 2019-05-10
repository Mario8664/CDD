package com.saveandstudio.mario.cdd.GameBasic;

import java.io.Serializable;

public class Vector2 extends Vector3 implements Cloneable{

    private float z;

    public Vector2()
    {
        this(0, 0);
    }

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Vector2 add(Vector2 vector2) {
        return new Vector2(x + vector2.x, y +vector2.y);
    }

    public Vector2 minus(Vector2 vector2){
        return new Vector2(x - vector2.x, y - vector2.y);
    }

    public Vector2 multiply(Vector2 vector2){
        return new Vector2(x * vector2.x, y * vector2.y);
    }

    public Vector2 divide(Vector2 vector2){
        return new Vector2(x / vector2.x, y / vector2.y);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Vector2)super.clone();
    }
}
