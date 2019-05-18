package com.saveandstudio.mario.cdd.Scenes;

import com.saveandstudio.mario.cdd.Components.TouchCardEvents;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.Prefabs.Card;
import com.saveandstudio.mario.cdd.Prefabs.Game;
import com.saveandstudio.mario.cdd.Prefabs.Player;

import java.util.ArrayList;

public class Scene {

    public static ArrayList<GameObject> gameObjectsList;

    public static void prePareScene() {
        Game game = new Game();
        Player player = new Player(0, new Transform(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH + 550, 0), 0,
                Vector3.one), true);
        Player Rplayer1 = new Player(1, new Transform(new Vector3(GameViewInfo.centerW + 400, GameViewInfo.centerH - 50, 0), 0,
                Vector3.one), false);
        Player Rplayer2 = new Player(0, new Transform(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH - 650, 0), 0,
                Vector3.one), false);
        Player Rplayer3 = new Player(1, new Transform(new Vector3(GameViewInfo.centerW - 400, GameViewInfo.centerH - 50, 0), 0,
                Vector3.one), false);
    }

    public static void Clear(){
        gameObjectsList.clear();
        for (int i = 0; i < gameObjectsList.size(); i++) {
            gameObjectsList.get(i).Destroy();
        }
    }

}
