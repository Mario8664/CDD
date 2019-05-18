package com.saveandstudio.mario.cdd.Scenes;

import com.saveandstudio.mario.cdd.Components.AutoCollider;
import com.saveandstudio.mario.cdd.Components.AutoPivot;
import com.saveandstudio.mario.cdd.Components.TransformToTarget;
import com.saveandstudio.mario.cdd.Components.TouchEvents;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.Prefabs.Card;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Renderers.CardRenderer;

import java.util.ArrayList;

public class Scene {

    public static ArrayList<GameObject> instantiateList;
    public static ArrayList<GameObject> gameObjectsList;

    public static void prePareScene() {
        Transform transform = new Transform(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH + 300, 0), 0,
                new Vector3((float) 1, (float)1, 0), Vector3.zero);
        GameObject cardDesk = new GameObject(transform);
        Card card = new Card(0, 0, new Transform(new Vector3(-40, 0, 2),0, Vector3.one,transform, Vector3.zero));
        Card card2 = new Card(3, 12, new Transform(new Vector3(0, 0, 3),0, Vector3.one,transform, Vector3.zero));
        Card card3 = new Card(2, 8, new Transform(new Vector3(40, 0, 4),0, Vector3.one,transform, Vector3.zero));
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
