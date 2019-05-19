package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.MonoBehavior;

public class AiPlayer extends MonoBehavior {
    HandCardManager manager;
    private long thinkTime = 2000;
    private long startThinkTime = 0;
    private boolean thinking = false;

    @Override
    public void Start() {
        manager = (HandCardManager)getComponent(HandCardManager.class);
    }

    public void Update(){
        if(manager.turn){
            if(!thinking){
                startThinkTime = System.currentTimeMillis();
                thinking = true;
            }
            else if(System.currentTimeMillis() - startThinkTime >= thinkTime){
                manager.passHandler();
                thinking = false;
            }

        }
    }
}
