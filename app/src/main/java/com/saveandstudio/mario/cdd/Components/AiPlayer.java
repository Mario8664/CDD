package com.saveandstudio.mario.cdd.Components;

import com.saveandstudio.mario.cdd.GameBasic.MonoBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AiPlayer extends MonoBehavior {
    HandCardManager manager;
    private ArrayList<Card> lastCards;
    private ArrayList<Card> handCards;
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
                handCards = manager.getCards();
                startThinkTime = System.currentTimeMillis();
                thinking = true;
            } else if (System.currentTimeMillis() - startThinkTime >= thinkTime) {
                manager.passHandler();
                thinking = false;
            }
        }
    }

    private boolean chooseCards() {
        //每一张牌都有权重，先把他们的权重归1
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            card.resetWeight();
        }
        //先取一张牌的组，存入cardPack_1
        for (int i = 0; i < handCards.size(); i++) {
            ArrayList<Card> cards = new ArrayList<>();
            cards.add(handCards.get(i));
            cardPack_1.add(cards);
        }
        //从头至倒数第一检阅cardPack_1，以检查对的存在（三连只能检测出两组）
        for (int i = 0; i < cardPack_1.size() - 1; i++) {
            Card card_1 = handCards.get(i);
            Card card_2 = handCards.get(i + 1);
            if (card_1.getFigure() == card_2.getFigure()) {
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(card_1);
                cards.add(card_2);
                cardPack_2.add(cards);
                //两张牌的单张出牌权重减少
                card_1.weights[0] *= decreaseRate;
                card_2.weights[0] *= decreaseRate;
            }
        }
        //从头至倒数第一检阅cardPack_2, 以检查三连的存在
        for (int i = 0; i < cardPack_2.size() - 1; i++) {
            //检查第i组的第0张和第i+1组的第1张，若相同，存入三张到cardPack_3并且也存入两张到cardPack_2以补充上面的
            Card card_1 = cardPack_2.get(i).get(0);
            Card card_3 = cardPack_2.get(i + 1).get(1);
            if (card_1.getFigure() == card_3.getFigure()) {
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(card_1);
                cards.add(cardPack_2.get(i).get(1));
                cards.add(card_3);
                cardPack_3.add(cards);
                //三张牌的两张出牌权重减少
                card_1.weights[1] *= decreaseRate;
                cardPack_2.get(i).get(1).weights[1] *= decreaseRate;
                card_3.weights[1] *= decreaseRate;

                //这里把两张牌补充至cardPack_2
                ArrayList<Card> cards_added = new ArrayList<>();
                cards_added.add(card_1);
                cards_added.add(card_3);
                cardPack_2.add(cards_added);
                //两张牌的单张出牌率减少
                card_1.weights[0] *= decreaseRate;
                card_3.weights[0] *= decreaseRate;
            }
        }
        //从头至倒数第一检阅cardPack_3, 以检查四连的存在
        for (int i = 0; i < cardPack_3.size() - 1; i++) {
            //检查第i组的第0张和第i+1组的第2张，若相同，存入四张到cardPack_4并且也存入三张到cardPack_3以补充上面的
            //并且，补充头尾两张至cardPack_2
            Card card_1 = cardPack_3.get(i).get(0);
            Card card_4 = cardPack_3.get(i + 1).get(2);
            if (card_1.getFigure() == card_4.getFigure()) {
                Card card_2 = cardPack_3.get(i).get(1);
                Card card_3 = cardPack_3.get(i).get(2);
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(card_1);
                cards.add(card_2);
                cards.add(card_3);
                cards.add(card_4);
                cardPack_4.add(cards);
                //四张牌的三张出牌权重减少
                card_1.weights[2] *= decreaseRate;
                card_2.weights[2] *= decreaseRate;
                card_3.weights[2] *= decreaseRate;
                card_4.weights[2] *= decreaseRate;

                //这里把三张牌补充至cardPack_3
                ArrayList<Card> cards_added_3_1 = new ArrayList<>();
                ArrayList<Card> cards_added_3_2 = new ArrayList<>();
                //先补充1、2、4张, 三张出牌权重减少
                cards_added_3_1.add(card_1);
                cards_added_3_1.add(card_2);
                cards_added_3_1.add(card_4);
                card_1.weights[2] *= decreaseRate;
                card_2.weights[2] *= decreaseRate;
                card_4.weights[2] *= decreaseRate;
                //再补充1、3、4张, 三张出牌权重减少
                cards_added_3_2.add(card_1);
                cards_added_3_2.add(card_3);
                cards_added_3_2.add(card_4);
                card_1.weights[2] *= decreaseRate;
                card_3.weights[2] *= decreaseRate;
                card_4.weights[2] *= decreaseRate;
                cardPack_3.add(cards_added_3_1);
                cardPack_3.add(cards_added_3_2);

                //这里把两张牌补充至cardPack_2
                ArrayList<Card> cards_added_2 = new ArrayList<>();
                //补充首尾两张, 两张出牌权重减少
                cards_added_2.add(card_1);
                cards_added_2.add(card_4);
                card_1.weights[1] *= decreaseRate;
                card_4.weights[1] *= decreaseRate;
                cardPack_2.add(cards_added_2);
            }
        }
        //这里按次序检查顺子、同花顺、同花、葫芦和金刚的存在

        //顺子
        ArrayList<ArrayList<Card>> cardBucket = new ArrayList<>();//建一个二维数组，每个列存入相同点数的牌
        for (int i = 0; i < handCards.size(); ) {
            ArrayList<Card> cardsFigureOnly = new ArrayList<>();
            cardsFigureOnly.add(handCards.get(i));
            i++;
            while (i < handCards.size()) {
                if (handCards.get(i).getFigure() == handCards.get(i - 1).getFigure()) {
                    cardsFigureOnly.add(handCards.get(i));
                    i++;
                } else {
                    break;
                }
            }
            cardBucket.add(cardsFigureOnly);
        }
        //连续检阅cardBucket每五项的第一张牌, 若点数递增则存入所有排列组合(不包括A2345和23456)
        for (int i = 0; i < cardBucket.size() - 4; i++) {
            ArrayList<Card> cardsSample = new ArrayList<>();//取第一张作为样本，因为后面的几张的数字完全相同
            for (int j = i; j < i + 5; j++) {
                cardsSample.add(cardBucket.get(j).get(0));
            }
            sequentialCardMatch(cardBucket, cardsSample, i);
        }
        //这里特判A2345和23456


        return false;
    }

    private void sequentialCardMatch(ArrayList<ArrayList<Card>> cardBucket, ArrayList<Card> cardsSample, int i){
        if (CardSystem.getInstance().judgeCardType(cardsSample) == 0) {//判定为顺子
            for (int i1 = 0; i1 < cardBucket.get(i).size(); i1++) {
                //第一层循环, 放入第一张
                ArrayList<Card> cards_1 = new ArrayList<>();
                cards_1.add(cardBucket.get(i).get(i1));
                for (int i2 = 0; i2 < cardBucket.get(i + 1).size(); i2++) {
                    //第二层循环, 放入第二张
                    ArrayList<Card> cards_2 = new ArrayList<>();
                    cards_2.add(cardBucket.get(i + 1).get(i2));
                    for (int i3 = 0; i3 < cardBucket.get(i + 2).size(); i3++) {
                        //第三层循环, 放入第三张
                        ArrayList<Card> cards_3 = new ArrayList<>();
                        cards_3.add(cardBucket.get(i + 2).get(i3));
                        for (int i4 = 0; i4 < cardBucket.get(i + 3).size(); i4++) {
                            //第四层循环, 放入第四张
                            ArrayList<Card> cards_4 = new ArrayList<>();
                            cards_4.add(cardBucket.get(i + 3).get(i4));
                            for (int i5 = 0; i5 < cardBucket.get(i + 4).size(); i5++) {
                                //第五层循环, 放入第五张
                                ArrayList<Card> cards_5 = new ArrayList<>();
                                cards_5.add(cardBucket.get(i + 3).get(i5));
                                //把以上数组合并
                                ArrayList<Card> cards = new ArrayList<>();
                                cards.addAll(cards_1);
                                cards.addAll(cards_2);
                                cards.addAll(cards_3);
                                cards.addAll(cards_4);
                                cards.addAll(cards_5);
                                //降低它们的1到4权重
                                for (int k = 0; k < cards.size(); k++) {
                                    for (int k1 = 0; k1 < cards.get(k).weights.length - 1; k1++) {
                                        cards.get(k).weights[k1] *= decreaseRate;
                                    }
                                }
                                cardPack_5.add(cards);
                                //若为同花顺，则升高这五张牌的五张出牌权重
                                if (CardSystem.getInstance().judgeCardType(cards) == 4) {
                                    for (int k = 0; k < cards.size(); k++) {
                                        cards.get(k).weights[4] /= decreaseRate;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




