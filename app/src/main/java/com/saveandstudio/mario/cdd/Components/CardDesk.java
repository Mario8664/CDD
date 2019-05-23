package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.MonoBehavior;
import com.saveandstudio.mario.cdd.GameBasic.Vector3;

public class CardDesk extends MonoBehavior {

    private int direction;//0 is horizontal, 1 is vertical;
    private float gapDistance = 45;

    public CardDesk(){
        this(0);
    }
    public CardDesk(int direction){
        this.direction = direction;
    }
    public Vector3 calculatePosition(int cardIndex, int cardAmount){
        Vector3 position = new Vector3();
        float centerCard = (float)cardAmount / 2;
        position.x = (((float)cardIndex - centerCard) * gapDistance) * (1 - direction);
        position.y = (((float)cardIndex - centerCard) * gapDistance) * direction;
        position.z = cardIndex;
        return position;
    }
    public Vector3 calculateOutPosition(int cardIndex, int cardAmount, int turn){
        Vector3 position = new Vector3();
        float centerCard = (float)cardAmount / 2;
        position.x = (((float)cardIndex - centerCard) * gapDistance);
        position.z = (cardIndex + 1) + turn * 13;
        return position;
    }
}
