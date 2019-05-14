package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.*;

public class AutoPivot extends MonoBehavior {
    private Transform transform;
    private Renderer renderer;

    @Override
    public void Start() {
        transform = (Transform)getComponent(Transform.class);
        renderer = (Renderer) getComponent(Renderer.class);
    }

    @Override
    public void Update() {
        if(renderer != null){
            transform.setPivot(new Vector3((float) renderer.bitMapWidth / 2, (float) renderer.bitMapHeight / 2, 0));
        }
    }
}
