package com.saveandstudio.mario.cdd.GameBasic;

import java.io.*;

public class Vector3 implements Cloneable{

    public static Vector3 zero = new Vector3(0,0,0), one = new Vector3(1,1,1);
    public float x;
    public float y;
    public float z;
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
        return new Vector3(x / vector3.x, y / vector3.y, z / vector3.z);
    }

    @Override
    protected Vector3 clone(){
        Vector3 vector3 = new Vector3(x, y, z);
        return vector3;
    }

    @Override
    public String toString() {
        return new String(Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z));
    }
}
