package com.saveandstudio.mario.cdd.Components;

import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.Prefabs.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CardSystem extends MonoBehavior {
    private ArrayList<com.saveandstudio.mario.cdd.Prefabs.Card> cards = new ArrayList<>();
    private ArrayList<com.saveandstudio.mario.cdd.Components.Card> lastCards = new ArrayList<>();
    private int cardAmount;
    private static CardSystem cardSystemInstance;
    private ArrayList<HandCardManager> players = new ArrayList<>();
    private int turn = 0;
    private int turnAmount = 0;

    @Override
    public void Awake() {
    }

    public static CardSystem getInstance() {
        if (cardSystemInstance == null) {
            cardSystemInstance = new CardSystem();
        }
        return cardSystemInstance;
    }

    @Override
    public void Start() {

    }

    @Override
    public void Update() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).turn = false;
        }
        players.get(turn).turn = true;
    }

    public com.saveandstudio.mario.cdd.Components.Card deliverCard() {
        com.saveandstudio.mario.cdd.Components.Card card = null;
        if (cardAmount >= 0) {
            card = (Card) cards.get(cardAmount).getComponent(Card.class);
            cardAmount--;
        }
        return card;
    }

    public boolean judgeCards(ArrayList<com.saveandstudio.mario.cdd.Components.Card> cards) {
        switch (cards.size()) {
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                boolean fit = true;
                if(turnAmount == 0){
                    for (int i = 0; i < cards.size(); i++) {
                        if(cards.get(i).getFigure() + cards.get(i).getSuit() == 0)
                            break;
                    }
                }
                else if (lastCards.size() == 4) {
                    if (cards.get(0).getFigure() == cards.get(1).getFigure() &&
                            cards.get(1).getFigure() == cards.get(2).getFigure() &&
                            cards.get(2).getFigure() == cards.get(3).getFigure())
                        return true;
                }
            case 5:
                return true;
            default:
                return false;
        }
    }

    public void showCards(ArrayList<com.saveandstudio.mario.cdd.Components.Card> cards) {
        lastCards.clear();
        lastCards.addAll(cards);
        turn = (turn + 1) % 4;
        turnAmount++;
    }

    public void pass() {
        turn = (turn + 1) % 4;
        turnAmount++;
    }

    public void addPlayer(HandCardManager player) {
        players.add(player);
        Collections.sort(players, new Comparator<HandCardManager>() {
            @Override
            public int compare(HandCardManager manager, HandCardManager t1) {
                int a = manager.getId(), b = t1.getId();
                if (a > b)
                    return 1;
                else
                    return -1;
            }
        });
    }

    public void newGame() {
        turnAmount = 0;
        players.clear();
        cards.clear();
        lastCards.clear();
        //Prepare cards
        cardAmount = -1;
        for (int suit = 0; suit <= 3; suit++) {
            for (int figure = 0; figure <= 12; figure++) {
                cards.add(new com.saveandstudio.mario.cdd.Prefabs.Card(suit, figure, new Transform(
                        new Vector3(GameViewInfo.centerW, GameViewInfo.centerH, 0),
                        0, Vector3.one)));
                cardAmount++;
            }
        }
        Collections.shuffle(cards);
    }

    public void setFirstTurn(int id) {
        turn = id;
    }

    public int getTurnAmount() {
        return turnAmount;
    }
}
