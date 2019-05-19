package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.*;

public class TouchCardEvents extends MonoBehavior {
    Card card;
    BoxCollider collider;
    Transform transform;
    TransformToTarget transformToTarget;
    private boolean selecting = false;
    private boolean canSet = true;

    @Override
    public void Start() {
        card = (Card) getComponent(Card.class);
        collider = (BoxCollider) getComponent(BoxCollider.class);
        transform = (Transform) getComponent(Transform.class);
        transformToTarget = (TransformToTarget) getComponent(TransformToTarget.class);
        selecting = false;
        canSet = true;
    }

    @Override
    public void Update() {
        if (card.intractable) {
            if (Input.touching && canSet) {
                if (collider != null) {
                    if (Physics.raycast(Input.touchPosition) == collider) {
                        selectAnimation();
                        canSet = false;
                    }
                }
            }
            if (Input.touchUp) {
                canSet = true;
            }
        }
    }

    private void selectAnimation() {
        Vector3 targetPosition = card.position.clone();
        if (selecting) {
            card.getManager().removeChosenCard(card);
            selecting = false;
        } else {
            card.getManager().addChosenCard(card);
            targetPosition.y -= 50;
            selecting = true;
        }
        transformToTarget.beginMove(targetPosition, 30 * GameViewInfo.deltaTime);
    }

}
