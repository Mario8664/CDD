package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.*;

import java.util.ArrayList;
import java.util.Collections;

public class HandCardManager extends MonoBehavior {
    private ArrayList<Card> handCards;
    private ArrayList<Card> outCards;
    private CardDesk cardDesk;
    private boolean isPlayer = false;
    private Transform transform;
    private boolean updatePosition = false;
    private ArrayList<Card> chosenCards;
    private int id;

    public boolean enableShowCard = false;
    public boolean enablePass = false;
    public boolean turn;

    public HandCardManager(){
        this(false, 100);
    }
    public HandCardManager(boolean isPlayer, int id){
        this.isPlayer = isPlayer;
        this.id = id;
    }

    @Override
    public void Start() {
        cardDesk = (CardDesk)getComponent(CardDesk.class);
        transform = (Transform)getComponent(Transform.class);
        chosenCards = new ArrayList<>();
        Vector3 position = transform.getPosition();
        handCards = new ArrayList<>();
        outCards = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Card card = CardSystem.getInstance().deliverCard();
            card.setManager(this);
            if(isPlayer)
            {
                card.addComponent(new BoxCollider());
                card.addComponent(new AutoCardCollider());
                card.addComponent(new TouchCardEvents());
                card.setSide(true);
            }
            handCards.add(card);
            if(card.getSuit() + card.getFigure() == 0){
                CardSystem.getInstance().setFirstTurn(id);
            }
        }
        Collections.sort(handCards);
        updatePositions();

    }

    public void updatePositions(){
            float speed = 30 * GameViewInfo.deltaTime;
            for (int i = 0; i < handCards.size(); i++) {
                Card card = handCards.get(i);
                card.position = transform.getPosition().add(
                        cardDesk.calculatePosition(i, handCards.size()));
                card.transformToTarget.beginMove(card.position, speed);
            }
    }

    public void  outCardAnimation(){
        float speed = 30 * GameViewInfo.deltaTime;
        for (int i = 0; i < outCards.size(); i++) {
            Card card = outCards.get(i);
            card.position = Vector3.lerp(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH, 0), transform.getPosition(), (float)0.2).add(
                    cardDesk.calculateOutPosition(i, outCards.size(), CardSystem.getInstance().getTurnAmount()));
            card.transformToTarget.beginMove(card.position, speed);
            card.intractable = false;
        }
    }

    public void clearOutCards(){
        //Animation
        float speed = 30 * GameViewInfo.deltaTime;
        for (int i = 0; i < outCards.size(); i++) {
            Card card = outCards.get(i);
            card.transformToTarget.beginScale(Vector3.zero, speed);
        }
        outCards.clear();

    }

    public void addChosenCard(Card card){
        chosenCards.add(card);
    }

    public void removeChosenCard(Card card){
        chosenCards.remove(card);
    }

    public void showCardHandler(){
        Collections.sort(chosenCards);
        clearOutCards();
        outCards.addAll(chosenCards);
        //Animation
        outCardAnimation();
        //Show
        CardSystem.getInstance().showCards(chosenCards);
        handCards.removeAll(chosenCards);
        updatePositions();
        chosenCards.clear();
        turn = false;
    }

    public void passHandler(){
        clearOutCards();
        CardSystem.getInstance().pass();
        chosenCards.clear();
        turn = false;
    }

    @Override
    public void Update(){
        enableShowCard = CardSystem.getInstance().judgeCards(chosenCards) && turn;
        enablePass = turn;
    }

    public int getId() {
        return id;
    }
}
