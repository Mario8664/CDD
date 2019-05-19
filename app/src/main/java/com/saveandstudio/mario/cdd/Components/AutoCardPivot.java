package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.Renderers.CardRenderer;

public class AutoCardPivot extends MonoBehavior {
    private Transform transform;
    private CardRenderer renderer;

    @Override
    public void Start() {
        transform = (Transform)getComponent(Transform.class);
        renderer = (CardRenderer) getComponent(CardRenderer.class);
    }

    @Override
    public void Update() {
        if(renderer != null){
            transform.setPivot(new Vector3((float) renderer.getBitMapWidth() / 2, (float) renderer.getBitMapHeight() / 2, 0));
            //transform.setRotation(transform.getRotation() + 100 * GameViewInfo.deltaTime);
        }
    }
}
