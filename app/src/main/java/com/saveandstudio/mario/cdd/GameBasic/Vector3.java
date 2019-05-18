package com.saveandstudio.mario.cdd.GameBasic;

import java.io.*;

public class Vector3 extends Vector2 implements Cloneable{

    public static Vector3 zero = new Vector3(0,0,0), one = new Vector3(1,1,1);
    public Vector3(){
        this(0, 0, 0);
    }
    public Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector3 add(Vector3 vector3){
        return new Vector3(x + vector3.x, y + vector3.y, z + vector3.z);
    }

    public Vector3 minus(Vector3 vector3){
        return new Vector3(x - vector3.x, y - vector3.y, z - vector3.z);
    }

    public Vector3 multiply(Vector3 vector3){
        return new Vector3(x * vector3.x, y * vector3.y, z * vector3.z);
    }

    public Vector3 divide(Vector3 vector3){
        vector3.x = Math.max((float) 0.0001, vector3.x);
        vector3.y = Math.max((float) 0.0001, vector3.y);
        vector3.z = Math.max((float) 0.0001, vector3.z);
        return new Vector3(x / vector3.x, y / vector3.y, z / vector3.z);
    }

    public boolean equal(Vector3 vector3){
        return (vector3.x == x && vector3.y == y && vector3.z == z);
    }

    @Override
    public Vector3 clone(){
        Vector3 vector3 = new Vector3(x, y, z);
        return vector3;
    }

    @Override
    public String toString() {
        return new String(Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z));
    }

    public static Vector3 lerp(Vector3 begin, Vector3 end, float time){
        Vector3 vector3 = new Vector3();
        time = Math.max(0, Math.min(time, 1));
        vector3.x = (end.x - begin.x) * time + begin.x;
        vector3.y = (end.y - begin.y) * time + begin.y;
        vector3.z = (end.z - begin.z) * time + begin.z;
        return vector3;
    }
}
