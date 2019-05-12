package com.saveandstudio.mario.cdd.Scenes;

import com.saveandstudio.mario.cdd.GameBasic.GameObject;
import com.saveandstudio.mario.cdd.GameBasic.Renderer;
import com.saveandstudio.mario.cdd.GameBasic.Transform;
import com.saveandstudio.mario.cdd.R;

public class CDDScene {


    public static void prePareScene(){
        GameObject gameObject = new GameObject(new Transform());
        gameObject.addComponent(new Renderer(R.mipmap.default_sprite));
    }
}
