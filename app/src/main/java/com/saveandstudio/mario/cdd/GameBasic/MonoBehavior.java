package com.saveandstudio.mario.cdd.GameBasic;

public class MonoBehavior {

    public GameObject gameObject;

    protected MonoBehavior()
    {
        Start();
    }

    protected void Start(){

    }

    protected void Update(){

    }

    public MonoBehavior getComponent(Class<? extends MonoBehavior> classInfo){
        return gameObject.getComponent(classInfo);
    }


}
