package com.saveandstudio.mario.cdd.Components;

import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Renderers.CardRenderer;

public class TouchTest extends MonoBehavior {
    CardRenderer renderer;
    BoxCollider collider;
    private boolean side;
    private boolean canSet = true;

    @Override
    public void Start() {
        renderer = (CardRenderer)getComponent(CardRenderer.class);
        collider = (BoxCollider)getComponent(BoxCollider.class);
        side = true;
        canSet = true;
    }

    @Override
    public void Update() {
        if(Input.touching && canSet){
            if(collider != null){
                if(Physics.raycast(Input.touchPosition) == collider){
                    if(renderer != null){
                        renderer.setSide(side);
                        Log.d("Game", Boolean.toString(side));
                        side = !side;
                        canSet = false;
                    }
                }
            }
        }
        if(Input.touchUp){
            canSet = true;
        }
    }
}
