package com.saveandstudio.mario.cdd.GameBasic;

import java.util.ArrayList;

public class GameObject {

    ArrayList<MonoBehavior> components;
    public GameObject(Transform transform){
        components = new ArrayList<MonoBehavior>();
        components.add(transform);
    }

    public void addComponent(MonoBehavior component){
        components.add(component);
        component.gameObject = this;
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

}
