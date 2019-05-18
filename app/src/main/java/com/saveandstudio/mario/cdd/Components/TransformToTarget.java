package com.saveandstudio.mario.cdd.Components;

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

    @Override
    public void Start() {
        transform = (Transform) getComponent(Transform.class);
    }

    @Override
    public void Update() {
        move();
        rotate();
        scale();
    }

    public void beginMove(Vector3 position, float moveSpeed){
        this.position = position;
        this.moveSpeed = moveSpeed;
        move = true;
    }

    private void move(){
        if(move){
            Vector3 originPosition = transform.getPosition();
            position.z = originPosition.z;
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
        if(scale){

        }
    }

}
