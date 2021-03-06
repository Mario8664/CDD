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
    private TransformToTarget passT;
    private Transform pass;

    public boolean enableShowCard = false;
    public boolean enablePass = false;
    public boolean turn;

    public HandCardManager(boolean isPlayer, int id, GameObject pass) {
        this.isPlayer = isPlayer;
        this.id = id;
        this.pass = (Transform) pass.getComponent(Transform.class);
        passT = (TransformToTarget) pass.getComponent(TransformToTarget.class);
    }

    @Override
    public void Start() {
        cardDesk = (CardDesk) getComponent(CardDesk.class);
        transform = (Transform) getComponent(Transform.class);
        chosenCards = new ArrayList<>();
        Vector3 position = transform.getPosition();
        handCards = new ArrayList<>();
        outCards = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Card card = CardSystem.getInstance().deliverCard();
            card.setManager(this);
            if (isPlayer) {
                card.addComponent(new BoxCollider());
                card.addComponent(new AutoCollider());
                card.addComponent(new TouchCardEvents());
                card.setSide(true);
            }

            handCards.add(card);

            Collections.sort(handCards);
            if (card.getSuit() + card.getFigure() == 0) {
                CardSystem.getInstance().setFirstTurn(id);
            }
        }
        updatePositions();
    }

    public void updatePositions() {
        float speed = 30 * GameViewInfo.deltaTime;
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            card.position = transform.getPosition().add(
                    cardDesk.calculatePosition(i, handCards.size()));
            card.transformToTarget.beginMove(card.position, speed);
        }
    }

    public void outCardAnimation() {
        float speed = 30 * GameViewInfo.deltaTime;
        for (int i = 0; i < outCards.size(); i++) {
            Card card = outCards.get(i);
            card.position = Vector3.lerp(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH - 100, 0), transform.getPosition(), (float) 0.3).add(
                    cardDesk.calculateOutPosition(i, outCards.size(), CardSystem.getInstance().getTurnAmount()));
            card.transformToTarget.beginMove(card.position, speed);
            card.intractable = false;
        }
        passT.beginScale(Vector3.zero, speed);
    }

    public void clearOutCards() {
        //Animation
        float speed = 30 * GameViewInfo.deltaTime;
        for (int i = 0; i < outCards.size(); i++) {
            Card card = outCards.get(i);
            card.transformToTarget.beginScale(Vector3.zero, speed);
        }
        pass.setScale(Vector3.zero);
        passT.beginScale(Vector3.one, speed);
        outCards.clear();

    }

    public void addChosenCard(Card card) {
        chosenCards.add(card);
        Collections.sort(chosenCards);
    }

    public void removeChosenCard(Card card) {
        chosenCards.remove(card);
        Collections.sort(chosenCards);
    }

    public void showCardHandler() {
        clearOutCards();
        outCards.addAll(chosenCards);
        //Animation
        outCardAnimation();
        //Judge
        CardSystem.getInstance().judgeCards(chosenCards);
        //Show
        for (int i = 0; i < chosenCards.size(); i++) {
            chosenCards.get(i).setSide(true);
        }
        CardSystem.getInstance().showCards(chosenCards);
        handCards.removeAll(chosenCards);
        updatePositions();
        chosenCards.clear();
        turn = false;
    }

    public void passHandler() {
        clearOutCards();
        CardSystem.getInstance().pass();
        turn = false;
    }

    @Override
    public void Update() {
        enableShowCard = CardSystem.getInstance().judgeCards(chosenCards) && turn;
        if (CardSystem.getInstance().getTurnAmount() == 0 || CardSystem.getInstance().getLastPlayerID() == id)
            enablePass = false;
        else
            enablePass = turn;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Card> getCards(){
        return handCards;
    }
}
