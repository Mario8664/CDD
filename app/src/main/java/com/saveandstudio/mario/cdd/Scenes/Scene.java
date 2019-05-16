package com.saveandstudio.mario.cdd.Scenes;

import com.saveandstudio.mario.cdd.Components.AutoCollider;
import com.saveandstudio.mario.cdd.Components.AutoPivot;
import com.saveandstudio.mario.cdd.Components.TouchTest;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Renderers.CardRenderer;

import java.util.ArrayList;

public class Scene {

    public static ArrayList<GameObject> instantiateList;
    public static ArrayList<GameObject> gameObjectsList;

    public static void prePareScene() {
        GameObject card = new GameObject(new Transform(new Vector3(GameViewInfo.centerW-60, GameViewInfo.centerH + 300, 2), 0,
                new Vector3((float) 0.3, (float)0.3, 0), Vector3.zero));
        card.addComponent(new CardRenderer(R.mipmap.diamond, R.mipmap.red_three));
        card.addComponent(new BoxCollider());
        card.addComponent(new AutoCollider());
        card.addComponent(new AutoPivot());
        card.addComponent(new TouchTest());
        GameObject card2 = new GameObject(new Transform(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH + 300, 3), 0,
                new Vector3((float) 0.3, (float)0.3, 0), Vector3.zero));
        card2.addComponent(new CardRenderer(R.mipmap.spade, R.mipmap.black_two));
        card2.addComponent(new BoxCollider());
        card2.addComponent(new AutoCollider());
        card2.addComponent(new AutoPivot());
        card2.addComponent(new TouchTest());
        GameObject card3 = new GameObject(new Transform(new Vector3(GameViewInfo.centerW + 60, GameViewInfo.centerH + 300, 4), 0,
                new Vector3((float) 0.3, (float)0.3, 0), Vector3.zero));
        card3.addComponent(new CardRenderer(R.mipmap.heart, R.mipmap.red_joker));
        card3.addComponent(new BoxCollider());
        card3.addComponent(new AutoCollider());
        card3.addComponent(new AutoPivot());
        card3.addComponent(new TouchTest());
    }

    public static void InstantiateStart() {
        if (instantiateList != null) {
            for (int i = 0; i < instantiateList.size(); i++) {
                GameObject tempGameObject = instantiateList.get(i);
                tempGameObject.Start();
                gameObjectsList.add(tempGameObject);
                instantiateList.remove(tempGameObject);
            }
        }
    }

    public static void Clear(){
        gameObjectsList.clear();
        for (int i = 0; i < gameObjectsList.size(); i++) {
            gameObjectsList.get(i).Destroy();
        }
    }

}
