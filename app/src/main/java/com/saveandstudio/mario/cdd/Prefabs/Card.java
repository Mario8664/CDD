package com.saveandstudio.mario.cdd.Prefabs;

import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.Components.*;
import com.saveandstudio.mario.cdd.Renderers.*;
import com.saveandstudio.mario.cdd.R;

public class Card extends GameObject {
    public Card(){
        this(0, 0);
    }
    public Card(int suit, int figure){
        this(suit, figure, new Transform());
    }
    public Card(int suit, int figure, Transform transform){
        super(transform);
        com.saveandstudio.mario.cdd.Components.Card card =
                (com.saveandstudio.mario.cdd.Components.Card) addComponent(new com.saveandstudio.mario.cdd.Components.Card());
        card.setCard(suit, figure);
        addComponent(new CardRenderer(R.mipmap.default_sprite, R.mipmap.default_sprite));
        addComponent(new AutoPivot());
        addComponent(new TransformToTarget());
    }
}
