package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.MonoBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AiPlayer extends MonoBehavior {
    HandCardManager manager;
    private ArrayList<Card> lastCards;
    private ArrayList<Card> cards1;
    private long thinkTime = 1000;
    private long startThinkTime = 0;
    private boolean thinking = false;
    private final float decreaseRate = (float) 0.9;//递减率
    private final float threshole = (float) 0.1;//出牌阈值

    private ArrayList<ArrayList<Card>> cardPack_1 = new ArrayList<>();
    private ArrayList<ArrayList<Card>> cardPack_2 = new ArrayList<>();
    private ArrayList<ArrayList<Card>> cardPack_3 = new ArrayList<>();
    private ArrayList<ArrayList<Card>> cardPack_4 = new ArrayList<>();
    private ArrayList<ArrayList<Card>> cardPack_5 = new ArrayList<>();

    @Override
    public void Start() {
        manager = (HandCardManager) getComponent(HandCardManager.class);
    }

    public void Update() {
        if (manager.turn) {
            if (!thinking) {
                lastCards = CardSystem.getInstance().getLastCards();
                startThinkTime = System.currentTimeMillis();
                thinking = true;
            } else if (System.currentTimeMillis() - startThinkTime >= thinkTime) {
                manager.passHandler();
                thinking = false;
            }
        }
    }

    private boolean chooseCards(){

        return false;
    }
}




