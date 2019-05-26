package com.saveandstudio.mario.cdd.Components;

import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.*;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Scenes.Scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CardSystem extends MonoBehavior {
    private ArrayList<com.saveandstudio.mario.cdd.Prefabs.Card> cards = new ArrayList<>();
    private ArrayList<com.saveandstudio.mario.cdd.Components.Card> lastCards = new ArrayList<>();
    private int cardAmount;
    private static CardSystem cardSystemInstance;
    private ArrayList<HandCardManager> players = new ArrayList<>();
    private int turn = 0;
    private int turnAmount = 0;
    private int lastPlayerID;
    private int lastCardType = 0;
    private Card lastMaxCard;
    private int thisCardType = 0;
    private Card thisMaxCard;
    private int cardNum = 52;
    private int[] cardNums;
    /*
     * 0、杂顺
     * 1、同花
     * 2、葫芦
     * 3、金刚
     * 4、同花顺
     * */
    public boolean someOneWin = false;

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
        if (turnAmount != 0 && turn != lastPlayerID && cards.size() != lastCards.size()) {
            return false;
        }
        switch (cards.size()) {
            case 1:
                if (turnAmount == 0)
                    return (cards.get(0).getSuit() + cards.get(0).getFigure() == 0);
                else if (turn == lastPlayerID)
                    return true;
                else
                    return (cards.get(0).compareTo(lastCards.get(0)) > 0);
            case 2:
                if (cards.get(0).getFigure() != cards.get(1).getFigure())
                    return false;
                if (turnAmount == 0) {
                    return (cards.get(0).getSuit() + cards.get(0).getFigure() == 0);
                } else if (turn == lastPlayerID)
                    return true;
                else {
                    return (cards.get(1).compareTo(lastCards.get(1)) > 0);
                }
            case 3:
                if (cards.get(0).getFigure() != cards.get(1).getFigure() || cards.get(1).getFigure() != cards.get(2).getFigure())
                    return false;
                if (turnAmount == 0) {
                    return (cards.get(0).getSuit() + cards.get(0).getFigure() == 0);
                } else if (turn == lastPlayerID)
                    return true;
                else {
                    return (cards.get(2).compareTo(lastCards.get(2)) > 0);
                }
            case 4:
                if (cards.get(0).getFigure() != cards.get(1).getFigure() ||
                        cards.get(1).getFigure() != cards.get(2).getFigure() ||
                        cards.get(2).getFigure() != cards.get(3).getFigure())
                    return false;
                if (turnAmount == 0) {
                    return (cards.get(0).getSuit() + cards.get(0).getFigure() == 0);
                } else if (turn == lastPlayerID)
                    return true;
                else {
                    return (cards.get(3).compareTo(lastCards.get(3)) > 0);
                }
            case 5:
                thisCardType = judgeCardType(cards);
                if (thisCardType == -1) {
                    return false;
                }
                if (turnAmount == 0) {
                    return (cards.get(0).getFigure() + cards.get(0).getSuit() == 0);
                }
                if (turn == lastPlayerID)
                    return true;
                if (thisCardType > lastCardType) {
                    return true;
                } else if (thisCardType == lastCardType) {
                    return (thisMaxCard.compareTo(lastMaxCard) > 0);
                }
            default:
                return false;
        }
    }

    public int judgeCardType(ArrayList<Card> cards) {
        int type = -1;
        int figure0 = cards.get(0).getFigure(), figure1 = cards.get(1).getFigure(), figure2 = cards.get(2).getFigure(),
                figure3 = cards.get(3).getFigure(), figure4 = cards.get(4).getFigure();
        int suit0 = cards.get(0).getSuit(), suit1 = cards.get(1).getSuit(), suit2 = cards.get(2).getSuit(),
                suit3 = cards.get(3).getSuit(), suit4 = cards.get(4).getSuit();
        if (figure0 + 1 == figure1 && figure1 + 1 == figure2 && figure2 + 1 == figure3 && figure3 + 1 == figure4 && figure4 != 12) {
            thisMaxCard = cards.get(4);
            type = 0;//杂顺
        } else if (figure0 == 0 && figure1 == 1 && figure2 == 2 && figure4 == 12) {
            if (figure3 == 11) {
                //345A2
                thisMaxCard = cards.get(4);
                type = 0;
            } else if (figure3 == 3) {
                //34562
                thisMaxCard = cards.get(4);
                type = 0;
            }
        }
        if (suit0 == suit1 && suit1 == suit2 && suit2 == suit3 && suit3 == suit4) {
            if (type == 0) {
                return 4;//同花顺
            } else {
                return 1;//同花
            }
        } else if (figure0 == figure3) {
            thisMaxCard = cards.get(3);
            return 3;//金刚4带1
        } else if (figure1 == figure4) {
            thisMaxCard = cards.get(4);
            return 3;//金刚1带4
        } else if (figure0 == figure2 && figure3 == figure4) {
            thisMaxCard = cards.get(2);
            return 2;//葫芦3带2
        } else if (figure2 == figure4 && figure0 == figure1) {
            thisMaxCard = cards.get(4);
            return 2;//葫芦2带3
        }
        return type;
    }

    public void showCards(ArrayList<com.saveandstudio.mario.cdd.Components.Card> cards) {
        if (cards.size() == 5) {
            lastCardType = thisCardType;
            lastMaxCard = thisMaxCard;
        }
        lastCards.clear();
        lastCards.addAll(cards);
        lastPlayerID = turn;
        cardNums[turn] -= cards.size();
        if (cardNums[turn] == 0) {
            someOneWin = true;
        }
        turn = (turn + 1) % 4;
        turnAmount++;
        cardNum -= cards.size();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).turn = false;
        }
        players.get(turn).turn = true;

    }

    public void showWinState() {
        synchronized (Renderer.renderersList) {
            Renderer.clear = true;
            if (Renderer.renderersList != null) {
                Renderer.renderersList.clear();
            }
        }
        Scene.getInstance().clearGameObjects();
        Physics.Clear();
        cards.clear();
        lastCards.clear();
        GameObject win = new GameObject(new Transform(new Vector3(GameViewInfo.centerW, GameViewInfo.centerH, 120), 0,
                new Vector3(0, 1, 0), Vector3.zero));
        win.addComponent(new AutoPivot());
        int state;
        if ((turn + 3) % 4 == 0) {
            state = R.mipmap.win;
        } else {
            state = R.mipmap.lose;

        }
        win.addComponent(new Renderer(state));
        ((TransformToTarget) win.addComponent(new TransformToTarget())).beginScale(Vector3.one, 20 * GameViewInfo.deltaTime);
    }

    public void pass() {
        turn = (turn + 1) % 4;
        turnAmount++;
        for (int i = 0; i < players.size(); i++) {
            players.get(i).turn = false;
        }
        players.get(turn).turn = true;
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
        someOneWin = false;
        turnAmount = 0;
        players.clear();
        cards.clear();
        lastCards.clear();
        cardNum = 52;
        cardNums = new int[4];
        for (int i = 0; i < 4; i++) {
            cardNums[i] = 13;
        }
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

    public void remove() {
        cardSystemInstance = null;
    }

    public void setFirstTurn(int id) {
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

        turn = id;
        for (int i = 0; i < players.size(); i++) {
            players.get(i).turn = false;
        }
        players.get(turn).turn = true;
    }

    public int getTurnAmount() {
        return turnAmount;
    }

    public ArrayList<Card> getLastCards() {
        return lastCards;
    }

    public int getLastCardType() {
        return lastCardType;
    }

    public Card getLastMaxCard() {
        return lastMaxCard;
    }

    public int getLastPlayerID() {
        return lastPlayerID;
    }

    public Card getThisMaxCard() {
        return thisMaxCard;
    }

    public int getCardNum() {
        return cardNum;
    }

    public int getCardNums(int i) {
        return cardNums[i];
    }
}
