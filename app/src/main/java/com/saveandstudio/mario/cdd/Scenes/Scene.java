package com.saveandstudio.mario.cdd.Scenes;

import com.saveandstudio.mario.cdd.Components.AutoCollider;
import com.saveandstudio.mario.cdd.Components.AutoPivot;
import com.saveandstudio.mario.cdd.Components.TouchTest;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;

import java.util.ArrayList;

public class Scene {

    public static ArrayList<GameObject> instantiateList;
    public static ArrayList<GameObject> gameObjectsList;

    public static void prePareScene() {
        GameObject gameObject = new GameObject(new Transform(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH, 0), 0,
                new Vector3(1, 1, 0), Vector3.zero));
        gameObject.addComponent(new Renderer(R.mipmap.button_up));
        gameObject.addComponent(new AutoPivot());
        gameObject.addComponent(new BoxCollider());
        gameObject.addComponent(new AutoCollider());
        gameObject.addComponent(new TouchTest());
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
