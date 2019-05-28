package com.saveandstudio.mario.cdd.GameBasic;

import android.util.Log;
import com.saveandstudio.mario.cdd.Scenes.Scene;

import java.util.ArrayList;

public class GameObject {

    private ArrayList<MonoBehavior> components;
    private ArrayList<MonoBehavior> newComponents;
    public boolean toBeDestroy;

    public GameObject(){
        this(new Transform());
    }

    public GameObject(Transform transform){
        if(Scene.getInstance().gameObjectsList == null){
            Scene.getInstance().gameObjectsList = new ArrayList<>();
        }
        Scene.getInstance().gameObjectsList.add(this);
        toBeDestroy = false;
        newComponents = new ArrayList<>();
        components = new ArrayList<>();
        newComponents.add(transform);
    }

    public MonoBehavior addComponent(MonoBehavior component){
        newComponents.add(component);
        component.gameObject = this;
        component.Awake();
        return component;
    }


    public MonoBehavior getComponent(Class<? extends MonoBehavior> classInfo){
        MonoBehavior component = null;
        for (int i = 0; i < components.size(); i++) {
            if(components.get(i).getClass().equals(classInfo) || (components.get(i).getClass().getSuperclass().equals(classInfo)
            && !components.get(i).getClass().getSuperclass().equals(MonoBehavior.class))){
                component = components.get(i);
            }
        }
        if(component == null)
        for (int i = 0; i < newComponents.size(); i++) {
            if(newComponents.get(i).getClass().equals(classInfo) || (newComponents.get(i).getClass().getSuperclass().equals(classInfo)
            && !newComponents.get(i).getClass().getSuperclass().equals(MonoBehavior.class))){
                component = newComponents.get(i);
            }
        }
        return component;
    }

    public void Start(){
        for (int i = 0; i < newComponents.size(); i++) {
            MonoBehavior component = newComponents.get(i);
            component.Start();
            components.add(component);
        }
        newComponents.clear();
    }

    public void Update(){
        Start();
        for (int i = 0; i < components.size(); i++) {
            components.get(i).Update();
        }
        if(timeDestroy){
            if(System.currentTimeMillis() - startTime >= destroyTime){
                Destroy();
                timeDestroy = false;
            }
        }
    }

    public void Destroy(){
        Scene.getInstance().gameObjectsList.remove(this);
        toBeDestroy = true;
    }

    private long startTime = 0;
    private boolean timeDestroy = false;
    private long destroyTime;

    public void Destroy(float time){
        startTime = System.currentTimeMillis();
        timeDestroy = true;
        destroyTime = (int)(time * 1000);
    }
}
