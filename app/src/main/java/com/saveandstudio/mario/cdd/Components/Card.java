package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.MonoBehavior;
import com.saveandstudio.mario.cdd.R;
import com.saveandstudio.mario.cdd.Renderers.CardRenderer;

public class Card extends MonoBehavior {
    int suit;
    int figure;
    CardRenderer renderer;
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
    }

    @Override
    public void Start() {
        renderer = (CardRenderer) getComponent(CardRenderer.class);
        if (renderer != null) {
            renderer.setCardID(mapSuitID(suit), mapFigureID(suit, figure));
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
    
    public int mapFigureID(int suit, int figure){
        if(suit % 2 == 0){
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
        }
        else {

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
}
