package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;

public class ShowCardButton extends MonoBehavior {

    BoxCollider collider;
    Transform transform;
    TransformToTarget transformToTarget;
    Renderer renderer;
    HandCardManager manager;
    private boolean waiting = false;
    private long startWaitTime = 0;
    private long waitTime = 1000;

    public ShowCardButton(HandCardManager manager) {
        this.manager = manager;
    }

    @Override
    public void Start() {
        collider = (BoxCollider) getComponent(BoxCollider.class);
        transform = (Transform) getComponent(Transform.class);
        transformToTarget = (TransformToTarget) getComponent(TransformToTarget.class);
        renderer = (Renderer) getComponent(Renderer.class);
        waiting = false;
    }

    @Override
    public void Update() {
        if (CardSystem.getInstance().someOneWin) {
            if (manager.turn) {
                if (!waiting) {
                    startWaitTime = System.currentTimeMillis();
                    waiting = true;
                } else {
                    if (System.currentTimeMillis() - startWaitTime >= waitTime) {
                        CardSystem.getInstance().showWinState();
                    }
                }
            }
        } else {
            if (!manager.enableShowCard) {
                renderer.setBitmapResource(R.mipmap.show_card_lock);
            } else {
                renderer.setBitmapResource(R.mipmap.show_card_up);
                if (Input.touching) {
                    if (collider != null) {
                        if (Physics.raycast(Input.touchPosition) == collider) {
                            renderer.setBitmapResource(R.mipmap.show_card_down);
                        } else {
                            renderer.setBitmapResource(R.mipmap.show_card_up);
                        }
                    }

                }
                if (Input.touchUp && Physics.raycast(Input.touchPosition) == collider) {
                    manager.showCardHandler();
                }

            }

        }

    }
}
