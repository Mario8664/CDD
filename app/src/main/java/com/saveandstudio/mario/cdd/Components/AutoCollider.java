package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.Renderers.CardRenderer;

public class AutoCollider extends MonoBehavior {
    private Transform transform;
    private BoxCollider collider;
    private Renderer renderer;

    @Override
    public void Start() {
        transform = (Transform)getComponent(Transform.class);
        collider = (BoxCollider) getComponent(BoxCollider.class);
        renderer = (Renderer) getComponent(Renderer.class);
    }

    @Override
    public void Update() {
        if(collider != null && renderer != null){
            collider.setOffset(Vector2.zero);
            collider.setSize(new Vector2((float) renderer.getBitMapWidth() * transform.getScale().x,
                    (float) renderer.getBitMapHeight() * transform.getScale().y));
        }
    }

}
