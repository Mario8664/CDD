package com.saveandstudio.mario.cdd.Components;

import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.MonoBehavior;
import com.saveandstudio.mario.cdd.GameBasic.Transform;
import com.saveandstudio.mario.cdd.GameBasic.Vector2;
import com.saveandstudio.mario.cdd.GameBasic.Vector3;

public class TransformToTarget extends MonoBehavior {

    private Transform transform;
    private boolean move = false;
    private boolean rotate = false;
    private boolean scale = false;
    //Move
    private Vector3 position;
    private float moveSpeed;
    //Rotate
    private float angle;
    private float rotateSpeed;
    //Scale
    private Vector3 targetScale;
    private float scaleSpeed;
    private boolean prepared = false;

    @Override
    public void Start() {
        transform = (Transform) getComponent(Transform.class);
        prepared = true;
    }

    @Override
    public void Update() {
        move();
        rotate();
        scale();
    }

    public void beginMove(Vector3 position, float moveSpeed){
        this.position = position.clone();
        this.moveSpeed = moveSpeed;
        move = true;
    }
    public void beginScale(Vector3 targetScale, float scaleSpeed){
        this.targetScale = targetScale.clone();
        this.scaleSpeed = scaleSpeed;
        scale = true;
    }

    private void move(){
        if(move && prepared){
            Vector3 originPosition = transform.getPosition();
            transform.setPosition(Vector3.lerp(originPosition, position, moveSpeed));
            if(originPosition.equal(position)){
                move = false;
            }

        }
    }

    private void rotate(){
        if(rotate){

        }
    }
    private void scale(){
        if(scale && prepared){
            Vector3 originScale = transform.getScale();
            transform.setScale(Vector3.lerp(originScale, targetScale, scaleSpeed));
            if(originScale.equal(targetScale)){
                scale = false;
            }
        }
    }

    public boolean isMove() {
        return move;
    }

    public boolean isRotate() {
        return rotate;
    }

    public boolean isScale() {
        return scale;
    }
}
