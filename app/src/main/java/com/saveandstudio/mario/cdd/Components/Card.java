package com.saveandstudio.mario.cdd.Components;

import android.support.annotation.NonNull;
import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.MonoBehavior;
import com.saveandstudio.mario.cdd.GameBasic.Transform;
import com.saveandstudio.mario.cdd.GameBasic.Vector3;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Renderers.CardRenderer;

public class Card extends MonoBehavior implements Comparable<Card> {
    private int suit;
    private int figure;
    private boolean side = false;
    private HandCardManager manager;
    public TransformToTarget transformToTarget;
    private Transform transform;
    public Vector3 position;
    public boolean intractable = true;
    CardRenderer renderer;
    public float[] weights;
    //suit
    final static int DIAMOND = 0;
    final static int CLUB = 1;
    final static int HEART = 2;
    final static int SPADE = 3;
    //figure
    final static int THREE = 0;
    final static int FOUR = 1;
    final static int FIVE = 2;
    final static int SIX = 3;
    final static int SEVEN = 4;
    final static int EIGHT = 5;
    final static int NINE = 6;
    final static int TEN = 7;
    final static int JOKER = 8;
    final static int QUEEN = 9;
    final static int KING = 10;
    final static int ACE = 11;
    final static int TWO = 12;

    public Card() {
        suit = DIAMOND;
        figure = THREE;
        weights = new float[5];
        resetWeight();
    }

    @Override
    public void Start() {
        renderer = (CardRenderer) getComponent(CardRenderer.class);
        if (renderer != null) {
            renderer.setCardID(mapSuitID(suit), mapFigureID(suit, figure));
        }
        transformToTarget = (TransformToTarget)getComponent(TransformToTarget.class);
        transform = (Transform)getComponent(Transform.class);
    }

    public void setSide(boolean side) {
        this.side = side;
    }

    @Override
    public void Update() {
        if (renderer != null) {
            renderer.setSide(side);
        }
    }

    public void setCard(int suit, int figure) {
        this.suit = suit;
        this.figure = figure;
    }

    private int mapSuitID(int suit) {
        switch (suit) {
            case DIAMOND:
                return R.mipmap.diamond;
            case CLUB:
                return R.mipmap.club;
            case HEART:
                return R.mipmap.heart;
            case SPADE:
                return R.mipmap.spade;
            default:
                return R.mipmap.default_sprite;
        }
    }

    @Override
    public int compareTo(@NonNull Card card) {
        if (figure > card.figure) {
            return 1;
        } else if (figure == card.figure) {
            if (suit > card.suit) {
                return 1;
            } else if (suit < card.suit) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public int mapFigureID(int suit, int figure) {
        if (suit % 2 == 0) {
            switch (figure) {
                case THREE:
                    return R.mipmap.red_three;
                case FOUR:
                    return R.mipmap.red_four;
                case FIVE:
                    return R.mipmap.red_five;
                case SIX:
                    return R.mipmap.red_six;
                case SEVEN:
                    return R.mipmap.red_seven;
                case EIGHT:
                    return R.mipmap.red_eight;
                case NINE:
                    return R.mipmap.red_nine;
                case TEN:
                    return R.mipmap.red_ten;
                case JOKER:
                    return R.mipmap.red_joker;
                case QUEEN:
                    return R.mipmap.red_queen;
                case KING:
                    return R.mipmap.red_king;
                case ACE:
                    return R.mipmap.red_ace;
                case TWO:
                    return R.mipmap.red_two;
                default:
                    return R.mipmap.default_sprite;
            }
        } else {

            switch (figure) {
                case THREE:
                    return R.mipmap.black_three;
                case FOUR:
                    return R.mipmap.black_four;
                case FIVE:
                    return R.mipmap.black_five;
                case SIX:
                    return R.mipmap.black_six;
                case SEVEN:
                    return R.mipmap.black_seven;
                case EIGHT:
                    return R.mipmap.black_eight;
                case NINE:
                    return R.mipmap.black_nine;
                case TEN:
                    return R.mipmap.black_ten;
                case JOKER:
                    return R.mipmap.black_joker;
                case QUEEN:
                    return R.mipmap.black_queen;
                case KING:
                    return R.mipmap.black_king;
                case ACE:
                    return R.mipmap.black_ace;
                case TWO:
                    return R.mipmap.black_two;
                default:
                    return R.mipmap.default_sprite;
            }
        }

    }

    public void setManager(HandCardManager manager) {
        this.manager = manager;
    }

    public HandCardManager getManager() {
        return manager;
    }

    public int getFigure() {
        return figure;
    }

    public int getSuit() {
        return suit;
    }

    public void resetWeight(){
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1;
        }
    }
}
