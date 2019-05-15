package com.saveandstudio.mario.cdd.GameBasic;

import java.io.Serializable;

public class Vector2 extends Vector3 implements Cloneable{

    public static Vector2 zero = new Vector2(0,0), one = new Vector2(1,1);
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
    protected Vector2 clone(){
        Vector2 vector2 = new Vector2(x, y);
        return vector2;
    }

    @Override
    public String toString() {
        return new String(Float.toString(x) + "," + Float.toString(y));
    }
}
