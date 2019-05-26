package com.saveandstudio.mario.cdd.Prefabs;

import com.saveandstudio.mario.cdd.Components.*;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;

public class Player extends GameObject {
    public Player(){
        this(0, new Transform(), false, 100);
    }
    public Player(int direction, Transform transform, boolean isPlayer, int id){
        super(transform);
        CardDesk cardDesk = (CardDesk) addComponent(new CardDesk(direction));

        GameObject pass = new GameObject(new Transform());
        ((Transform)pass.getComponent(Transform.class)).setScale(Vector3.zero);
        Vector3 position = Vector3.lerp(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH - 100, 0), transform.getPosition(), (float) 0.5).add(
                cardDesk.calculateOutPosition(0, 1, 0));
        pass.addComponent(new Renderer(R.mipmap.pass));
        pass.addComponent(new TransformToTarget());
        position.z = -120;
        ((Transform)pass.getComponent(Transform.class)).setPosition(position);
        pass.addComponent(new AutoPivot());


        CardSystem.getInstance().addPlayer((HandCardManager)addComponent(new HandCardManager(isPlayer, id, pass)));
    }
}
