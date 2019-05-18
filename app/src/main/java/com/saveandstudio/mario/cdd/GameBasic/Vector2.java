package com.saveandstudio.mario.cdd.GameBasic;


public class Vector2 implements Cloneable{

    public static Vector2 zero = new Vector2(0,0), one = new Vector2(1,1);
    public float x;
    public float y;
    public float z;

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

    public boolean equal(Vector2 vector2){
        return (vector2.x == x && vector2.y == y);
    }

    @Override
    public Vector2 clone(){
        Vector2 vector2 = new Vector2(x, y);
        return vector2;
    }

    @Override
    public String toString() {
        return new String(Float.toString(x) + "," + Float.toString(y));
    }

    public static Vector2 lerp(Vector2 begin, Vector2 end, float time){
        Vector2 vector2 = new Vector2();
        time = Math.max(0, Math.min(time, 1));
        vector2.x = (end.x - begin.x) * time + begin.x;
        vector2.y = (end.y - begin.y) * time + begin.y;
        return vector2;
    }
}
