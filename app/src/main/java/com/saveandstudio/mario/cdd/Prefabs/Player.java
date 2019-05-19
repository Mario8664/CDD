package com.saveandstudio.mario.cdd.Prefabs;

import com.saveandstudio.mario.cdd.Components.CardSystem;
import com.saveandstudio.mario.cdd.Components.HandCardManager;
import com.saveandstudio.mario.cdd.Components.CardDesk;
import com.saveandstudio.mario.cdd.GameBasic.GameObject;
import com.saveandstudio.mario.cdd.GameBasic.Transform;

public class Player extends GameObject {
    public Player(){
        this(0, new Transform(), false, 100);
    }
    public Player(int direction, Transform transform, boolean isPlayer, int id){
        super(transform);
        addComponent(new CardDesk(direction));
        CardSystem.getInstance().addPlayer((HandCardManager)addComponent(new HandCardManager(isPlayer, id)));
    }
}
