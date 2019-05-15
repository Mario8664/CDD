package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;

public class TouchTest extends MonoBehavior {
    Renderer renderer;
    BoxCollider collider;

    @Override
    public void Start() {
        renderer = (Renderer)getComponent(Renderer.class);
        collider = (BoxCollider)getComponent(BoxCollider.class);
    }

    @Override
    public void Update() {
        if(Input.touchDown){
            if(collider != null){
                if(Physics.raycast(Input.touchPosition) == collider){
                    if(renderer != null){
                        renderer.setBitmapResource(R.mipmap.button_down);
                    }
                }
            }
        }
        if(!Input.touching){
            if(renderer != null){
                renderer.setBitmapResource(R.mipmap.button_up);
            }
        }
    }
}
