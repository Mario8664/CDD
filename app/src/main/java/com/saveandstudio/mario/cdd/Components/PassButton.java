package com.saveandstudio.mario.cdd.Components;

import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Renderers.ButtonRenderer;

public class PassButton extends MonoBehavior {

    BoxCollider collider;
    Transform transform;
    TransformToTarget transformToTarget;
    ButtonRenderer renderer;
    HandCardManager manager;

    public PassButton(HandCardManager manager){
        this.manager = manager;
    }

    @Override
    public void Start() {
        collider = (BoxCollider)getComponent(BoxCollider.class);
        transform = (Transform)getComponent(Transform.class);
        transformToTarget = (TransformToTarget)getComponent(TransformToTarget.class);
        renderer = (ButtonRenderer)getComponent(ButtonRenderer.class);
    }

    @Override
    public void Update() {
        if(!manager.enablePass){
            renderer.setDisable();
        }
        else {
            renderer.setNormal();
            if(Input.touching){
                if(collider != null){
                    if(Physics.raycast(Input.touchPosition) == collider){
                        renderer.setFocus();
                    }
                    else {
                        renderer.setNormal();
                    }
                }

            }
            if(Input.touchUp && Physics.raycast(Input.touchPosition) == collider){
                manager.passHandler();
            }
        }
    }

}
