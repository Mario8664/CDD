package com.saveandstudio.mario.cdd.GameBasic;

import java.util.HashMap;

public class RenderList {
    private static RenderList instance;
    private RenderList() {}
    public static RenderList getInstance()
    {
        if(instance == null)
            instance = new RenderList();
        return instance;
    }

}
