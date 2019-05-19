package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;

public class PassButton extends MonoBehavior {

    BoxCollider collider;
    Transform transform;
    TransformToTarget transformToTarget;
    Renderer renderer;
    HandCardManager manager;

    public PassButton(HandCardManager manager){
        this.manager = manager;
    }

    @Override
    public void Start() {
        collider = (BoxCollider)getComponent(BoxCollider.class);
        transform = (Transform)getComponent(Transform.class);
        transformToTarget = (TransformToTarget)getComponent(TransformToTarget.class);
        renderer = (Renderer)getComponent(Renderer.class);
    }

    @Override
    public void Update() {
        if(!manager.enablePass){
            renderer.setBitmapResource(R.mipmap.pass_lock);
        }
        else {
            renderer.setBitmapResource(R.mipmap.pass_up);
            if(Input.touching){
                if(collider != null){
                    if(Physics.raycast(Input.touchPosition) == collider){
                        renderer.setBitmapResource(R.mipmap.pass_down);
                    }
                    else {
                        renderer.setBitmapResource(R.mipmap.pass_up);
                    }
                }

            }
            if(Input.touchUp && Physics.raycast(Input.touchPosition) == collider){
                manager.passHandler();
            }
        }
    }

}
