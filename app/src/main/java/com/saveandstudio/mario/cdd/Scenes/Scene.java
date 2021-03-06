package com.saveandstudio.mario.cdd.Scenes;

import com.saveandstudio.mario.cdd.Components.*;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.Prefabs.Game;
import com.saveandstudio.mario.cdd.Prefabs.Player;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Renderers.ButtonRenderer;

import java.util.ArrayList;

public class Scene {

    public ArrayList<GameObject> gameObjectsList;
    public static boolean prepared = false;
    public boolean clear = false;

    private static Scene instantce;
    public static Scene getInstance(){
        if(instantce == null){
            instantce = new Scene();
            prepared = false;
        }
        return instantce;
    }

    public void prePareScene() {
        clear = false;
        Game game = new Game();
        Player player = new Player(0, new Transform(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH + 550, 0), 0,
                Vector3.one), true, 0);
        //player.addComponent(new AiPlayer());
        Player Rplayer1 = new Player(1, new Transform(new Vector3(GameViewInfo.centerW + 400, GameViewInfo.centerH - 50, 0), 0,
                Vector3.one), false, 1);
        Rplayer1.addComponent(new AiPlayer());
        Player Rplayer2 = new Player(0, new Transform(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH - 650, 0), 0,
                Vector3.one), false, 2);
        Rplayer2.addComponent(new AiPlayer());
        Player Rplayer3 = new Player(1, new Transform(new Vector3(GameViewInfo.centerW - 400, GameViewInfo.centerH - 50, 0), 0,
                Vector3.one), false, 3);
        Rplayer3.addComponent(new AiPlayer());
        //show card button
        GameObject showCardButton = new GameObject(new Transform(new Vector3(GameViewInfo.centerW + 170, GameViewInfo.centerH + 320, 100), 0,
                new Vector3((float) 1.5, (float)1.5,1), Vector3.zero));
        showCardButton.addComponent(new ButtonRenderer(R.mipmap.show_card_up, R.mipmap.show_card_down, R.mipmap.show_card_lock));
        showCardButton.addComponent(new AutoPivot());
        showCardButton.addComponent(new BoxCollider());
        showCardButton.addComponent(new AutoCollider());
        showCardButton.addComponent(new ShowCardButton((HandCardManager)player.getComponent(HandCardManager.class)));
        //pass button
        GameObject passButton = new GameObject(new Transform(new Vector3(GameViewInfo.centerW - 170, GameViewInfo.centerH + 320, 100), 0,
                new Vector3((float) 1.5, (float)1.5,1), Vector3.zero));
        passButton.addComponent(new ButtonRenderer(R.mipmap.pass_up, R.mipmap.pass_down, R.mipmap.pass_lock));
        passButton.addComponent(new AutoPivot());
        passButton.addComponent(new BoxCollider());
        passButton.addComponent(new AutoCollider());
        passButton.addComponent(new PassButton((HandCardManager)player.getComponent(HandCardManager.class)));
        prepared = true;
    }

    public void Clear(){
        if(clear){
            clearGameObjects();
            clear = false;
            instantce = null;
        }
    }
    public void clearGameObjects(){
            for (int i = 0; i < gameObjectsList.size(); i++) {
                gameObjectsList.get(i).Destroy();
            }
            gameObjectsList.clear();
            instantce = null;

    }

}
