package com.saveandstudio.mario.cdd.GameBasic;

import android.graphics.Matrix;

import java.util.ArrayList;

public class Transform extends MonoBehavior{
    private static Transform world = new Transform(null);
    private Vector3 position = new Vector3();
    private Vector3 localPosition = new Vector3();
    private float rotation;
    private float localRotation;
    private Vector3 scale = new Vector3();
    private Vector3 localScale = new Vector3();
    private Vector3 pivot = new Vector3();
    public Matrix transformMatrix = new Matrix();
    private Transform parent;
    private ArrayList<Transform> children = new ArrayList<>();

    public Transform(Transform parent){
        position = Vector3.zero;
        localPosition = Vector3.zero;
        rotation = 0;
        localRotation = 0;
        localScale = Vector3.one;
        scale = Vector3.one;
        updateMatrix();
    }

    public Transform(){
        this(Vector3.zero, 0, Vector3.zero);
    }

    public Transform(Vector3 position, float rotation, Vector3 pivot){
        this(position, rotation, Vector3.one, pivot);
    }

    public Transform(Vector3 position, float rotation, Vector3 localScale, Vector3 pivot){
        this(position, rotation, world, localScale, pivot);
    }
    public Transform(Vector3 position, float rotation, Transform parent, Vector3 localScale, Vector3 pivot){
        this.position = position.clone();
        this.localPosition = position.minus(parent.getPosition());
        this.rotation = rotation;
        this.localRotation = rotation - parent.rotation;
        this.localScale = localScale.clone();
        this.scale = localScale.multiply(parent.getScale());
        this.pivot = pivot.clone();
        updateMatrix();
        this.parent = parent;
        this.parent.addChildren(this);
    }
    public Transform(Vector3 localPosition, float localRotation, Vector3 localScale, Transform parent, Vector3 pivot){
        this.position = localPosition.add(parent.position);
        this.localPosition = localPosition.clone();
        this.rotation = localRotation + parent.rotation;
        this.localRotation = localRotation;
        this.localScale = localScale.clone();
        this.scale = localScale.multiply(parent.getScale());
        this.pivot = pivot.clone();
        updateMatrix();
        this.parent = parent;
        this.parent.addChildren(this);
    }
    private void addChildren(Transform transform){
        children.add(transform);
    }
    public Transform getChild(int index){
        if(children.size() > 0)
            return children.get(0);
        return null;
    }
    //Get transform
    public Transform getParent(){
        return parent;
    }

    public Vector3 getPosition() {
        return position.clone();
    }

    public float getRotation() {
        return rotation;
    }

    public Vector3 getLocalPosition() {
        return localPosition.clone();
    }

    public float getLocalRotation() {
        return localRotation;
    }

    public Vector3 getLocalScale() {
        return localScale.clone();
    }

    public Vector3 getScale() {
        return scale.clone();
    }

    public Vector3 getPivot() {
        return pivot;
    }

    //Set transform

    public void setParent(Transform parent) {
        this.parent = parent;
        this.localPosition = position.minus(parent.getPosition());
        this.localRotation = rotation - parent.getRotation();
        this.localScale = scale.divide(parent.getScale());
    }

    public void setPosition(Vector3 position) {
        this.position = position.clone();
        this.localPosition = position.minus(parent.getPosition());
        updateMatrix();
    }

    public void setLocalPosition(Vector3 localPosition) {
        this.localPosition = localPosition.clone();
        this.position = localPosition.add(parent.getPosition());
        updateMatrix();
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        this.localRotation = rotation - parent.getRotation();
        updateMatrix();
    }

    public void setLocalRotation(float localRotation) {
        this.localRotation = localRotation;
        this.rotation = localRotation + parent.getRotation();
        updateMatrix();
    }

    public void setLocalScale(Vector3 localScale) {
        this.localScale = localScale.clone();
        this.scale = localScale.multiply(parent.getScale());
        updateMatrix();
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
        this.localScale = scale.divide(parent.getScale());
        updateMatrix();
    }

    public void setPivot(Vector3 pivot) {
        this.pivot = pivot.clone();
        updateMatrix();
    }

    private void updateMatrix(){

        if(parent != null){
            position = localPosition.add(parent.getPosition());
            rotation = localRotation + parent.getRotation();
            scale = localScale.multiply(parent.getScale());
        }

        transformMatrix.setTranslate(position.x * GameViewInfo.screenW / GameViewInfo.fixedW - pivot.x ,
                position.y * GameViewInfo.screenH / GameViewInfo.fixedH - pivot.y);
        transformMatrix.preRotate(rotation, pivot.x, pivot.y);
        transformMatrix.preScale(scale.x,
                scale.y,
                pivot.x,
                pivot.y);
    }

    public void Update(){
        updateMatrix();
    }
}
