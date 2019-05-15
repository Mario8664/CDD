package com.saveandstudio.mario.cdd.GameBasic;

import android.util.Log;
import com.saveandstudio.mario.cdd.Scenes.Scene;

import java.util.ArrayList;

public class GameObject {

    private ArrayList<MonoBehavior> components;
    boolean toBeDestroy;

    public GameObject(Transform transform){
        if(Scene.gameObjectsList == null){
            Scene.gameObjectsList = new ArrayList<>();
        }
        if(Scene.instantiateList == null){
            Scene.instantiateList = new ArrayList<>();
        }
        Scene.instantiateList.add(this);
        toBeDestroy = false;
        components = new ArrayList<MonoBehavior>();
        components.add(transform);
    }

    public void addComponent(MonoBehavior component){
        components.add(component);
        component.gameObject = this;
        component.Awake();
    }


    public MonoBehavior getComponent(Class<? extends MonoBehavior> classInfo){
        MonoBehavior component = null;
        for (int i = 0; i < components.size(); i++) {
            if(components.get(i).getClass().equals(classInfo)){
                component = components.get(i);
            }
        }
        return component;
    }

    public void Start(){
        for (int i = 0; i < components.size(); i++) {
            components.get(i).Start();
        }
    }

    public void Update(){
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
        Scene.gameObjectsList.remove(this);
        toBeDestroy = true;
        Log.d("Game", "fff!");
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
