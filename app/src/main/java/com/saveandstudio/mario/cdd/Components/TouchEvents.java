package com.saveandstudio.mario.cdd.Components;

import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Renderers.CardRenderer;

public class TouchEvents extends MonoBehavior {
    CardRenderer renderer;
    BoxCollider collider;
    Transform transform;
    TransformToTarget transformToTarget;
    private boolean selecting = false;
    private boolean canSet = true;
    private Vector3 originPosition;

    @Override
    public void Start() {
        renderer = (CardRenderer)getComponent(CardRenderer.class);
        collider = (BoxCollider)getComponent(BoxCollider.class);
        transform = (Transform)getComponent(Transform.class);
        originPosition = transform.getPosition();
        transformToTarget = (TransformToTarget)getComponent(TransformToTarget.class);
        selecting = false;
        canSet = true;
    }

    @Override
    public void Update() {
        if(Input.touching && canSet){
            if(collider != null){
                if(Physics.raycast(Input.touchPosition) == collider){
                    select();
                    canSet = false;
                }
            }
        }
        if(Input.touchUp){
            canSet = true;
        }
    }

    private void select(){
        Vector3 targetPosition = originPosition.clone();
        if(selecting){
            selecting = false;
        }
        else{
            targetPosition.y -= 50;
            selecting = true;
        }
        transformToTarget.beginMove(targetPosition, 30 * GameViewInfo.deltaTime);


    }
}
