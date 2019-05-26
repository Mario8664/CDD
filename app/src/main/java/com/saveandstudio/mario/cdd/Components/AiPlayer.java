package com.saveandstudio.mario.cdd.Components;

import android.util.Log;
import com.saveandstudio.mario.cdd.GameBasic.MonoBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//
public class AiPlayer extends MonoBehavior {
    HandCardManager manager;
    private ArrayList<Card> lastCards;
    private ArrayList<Card> handCards;
    private long thinkTime = 1000;
    private long startThinkTime = 0;
    private boolean thinking = false;
    private final float decreaseRate_small = (float) 0.9;//小递减率
    private final float decreaseRate_big = (float) 0.3;//大递减率
    private final float decreaseGap = (float) 0.1;//递减值
    private final float threshold = (float) 0.9;//出牌阈值
    private final int threshold_MAX = 3;//顶大阈值

    private float threshold_used = threshold;//出牌阈值

    //一个简简单单容易输的AI
    //也许以后会用机器学习训练
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
                if(CardSystem.getInstance().someOneWin){
                    CardSystem.getInstance().showWinState();
                }
                else if (chooseCards())
                    manager.showCardHandler();
                else
                    manager.passHandler();
                thinking = false;
            }
        }
    }

    private boolean chooseCards() {
        int minCardNum = 14;
        for (int i = 0; i < 4; i++) {
            minCardNum = Math.min(CardSystem.getInstance().getCardNums(i), minCardNum);
        }
        //新建cardPack
        ArrayList<ArrayList<Card>> cardPack_1 = new ArrayList<>();
        ArrayList<ArrayList<Card>> cardPack_2 = new ArrayList<>();
        ArrayList<ArrayList<Card>> cardPack_3 = new ArrayList<>();
        ArrayList<ArrayList<Card>> cardPack_4 = new ArrayList<>();
        ArrayList<ArrayList<Card>> cardPack_5 = new ArrayList<>();
        ArrayList<Card> lastCards = CardSystem.getInstance().getLastCards();
        int lastCardSize = lastCards.size();
        threshold_used = threshold;
        threshold_used -= minCardNum * decreaseGap;
        //每一张牌都有权重，先把他们的权重归1
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            card.resetWeight();
        }
        //先取一张牌的组，存入cardPack_1
        for (int i = 0; i < handCards.size(); i++) {
            ArrayList<Card> cards = new ArrayList<>();
            Card card = handCards.get(i);
            //比上一张牌小的直接出牌权重为0
            if (lastCardSize == 1) {
                if (card.compareTo(lastCards.get(0)) <= 0) {
                    card.weights[0] = 0;
                }
            }
            cards.add(card);
            cardPack_1.add(cards);
        }
        //从头至倒数第一检阅cardPack_1，以检查对的存在（三连只能检测出两组）
        for (int i = 0; i < cardPack_1.size() - 1; i++) {
            Card card_1 = cardPack_1.get(i).get(0);
            Card card_2 = cardPack_1.get(i + 1).get(0);
            if (card_1.getFigure() == card_2.getFigure()) {
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(card_1);
                cards.add(card_2);
                cardPack_2.add(cards);
                //两张牌的单张出牌权重减少
                card_1.weights[0] *= decreaseRate_small;
                card_2.weights[0] *= decreaseRate_small;
                //比上一张牌小的直接出牌权重为0
                if (lastCardSize == 2) {
                    if (card_2.compareTo(lastCards.get(1)) <= 0) {
                        card_1.weights[1] = 0;
                        card_2.weights[1] = 0;
                    }
                }
            }
        }
        //从头至倒数第一检阅cardPack_2, 以检查三连的存在
        int cardPack_2_Size = cardPack_2.size();
        for (int i = 0; i < cardPack_2_Size - 1; i++) {
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
                card_1.weights[1] *= decreaseRate_small;
                cardPack_2.get(i).get(1).weights[1] *= decreaseRate_small;
                card_3.weights[1] *= decreaseRate_small;
                //比上一张牌小的直接出牌权重为0
                if (lastCardSize == 3) {
                    if (card_3.compareTo(lastCards.get(2)) <= 0) {
                        card_1.weights[2] = 0;
                        cardPack_2.get(i).get(1).weights[2] = 0;
                        card_3.weights[2] = 0;
                    }
                }

                //这里把两张牌补充至cardPack_2
                ArrayList<Card> cards_added = new ArrayList<>();
                cards_added.add(card_1);
                cards_added.add(card_3);
                cardPack_2.add(cards_added);
                //两张牌的单张出牌率减少
                card_1.weights[0] *= decreaseRate_small;
                card_3.weights[0] *= decreaseRate_small;
                //比上一张牌小的直接出牌权重为0
                if (lastCardSize == 2) {
                    if (card_3.compareTo(lastCards.get(1)) <= 0) {
                        card_1.weights[1] = 0;
                        card_3.weights[1] = 0;
                    }
                }
            }
        }
        //从头至倒数第一检阅cardPack_3, 以检查四连的存在
        int cardPack_3_Size = cardPack_3.size();
        for (int i = 0; i < cardPack_3_Size - 1; i++) {
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
                card_1.weights[2] *= decreaseRate_small;
                card_2.weights[2] *= decreaseRate_small;
                card_3.weights[2] *= decreaseRate_small;
                card_4.weights[2] *= decreaseRate_small;
                //比上一张牌小的直接出牌权重为0
                if (lastCardSize == 4) {
                    if (card_4.compareTo(lastCards.get(3)) <= 0) {
                        card_1.weights[3] = 0;
                        card_2.weights[3] = 0;
                        card_3.weights[3] = 0;
                        card_4.weights[3] = 0;
                    }
                }

                //这里把三张牌补充至cardPack_3
                ArrayList<Card> cards_added_3_1 = new ArrayList<>();
                ArrayList<Card> cards_added_3_2 = new ArrayList<>();
                //先补充1、2、4张, 三张出牌权重减少
                cards_added_3_1.add(card_1);
                cards_added_3_1.add(card_2);
                cards_added_3_1.add(card_4);
                card_1.weights[2] *= decreaseRate_small;
                card_2.weights[2] *= decreaseRate_small;
                card_4.weights[2] *= decreaseRate_small;
                //再补充1、3、4张, 三张出牌权重减少
                cards_added_3_2.add(card_1);
                cards_added_3_2.add(card_3);
                cards_added_3_2.add(card_4);
                card_1.weights[2] *= decreaseRate_small;
                card_3.weights[2] *= decreaseRate_small;
                card_4.weights[2] *= decreaseRate_small;
                //比上一张牌小的直接出牌权重为0
                if (lastCardSize == 3) {
                    if (card_4.compareTo(lastCards.get(2)) <= 0) {
                        card_1.weights[2] = 0;
                        card_2.weights[2] = 0;
                        card_3.weights[2] = 0;
                        card_4.weights[2] = 0;
                    }
                }

                cardPack_3.add(cards_added_3_1);
                cardPack_3.add(cards_added_3_2);

                //这里把两张牌补充至cardPack_2
                ArrayList<Card> cards_added_2 = new ArrayList<>();
                //补充首尾两张, 两张出牌权重减少
                cards_added_2.add(card_1);
                cards_added_2.add(card_4);
                card_1.weights[1] *= decreaseRate_small;
                card_4.weights[1] *= decreaseRate_small;
                //比上一张牌小的直接出牌权重为0
                if (lastCardSize == 2) {
                    if (card_4.compareTo(lastCards.get(1)) <= 0) {
                        card_1.weights[1] = 0;
                        card_4.weights[1] = 0;
                    }
                }
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
            sequentialCardMatch(cardPack_5, cardBucket, cardsSample, i, i + 1, i + 2, i + 3, i + 4);
        }
        //这里特判A2345和23456
        if (cardBucket.size() >= 5) {
            ArrayList<Card> cardsSample_A2345 = new ArrayList<>();
            cardsSample_A2345.add(cardBucket.get(0).get(0));
            cardsSample_A2345.add(cardBucket.get(1).get(0));
            cardsSample_A2345.add(cardBucket.get(2).get(0));
            cardsSample_A2345.add(cardBucket.get(cardBucket.size() - 2).get(0));
            cardsSample_A2345.add(cardBucket.get(cardBucket.size() - 1).get(0));
            sequentialCardMatch(cardPack_5, cardBucket, cardsSample_A2345, 0, 1, 2, cardBucket.size() - 2, cardBucket.size() - 1);

            ArrayList<Card> cardsSample_23456 = new ArrayList<>();
            cardsSample_23456.add(cardBucket.get(0).get(0));
            cardsSample_23456.add(cardBucket.get(1).get(0));
            cardsSample_23456.add(cardBucket.get(2).get(0));
            cardsSample_23456.add(cardBucket.get(3).get(0));
            cardsSample_23456.add(cardBucket.get(cardBucket.size() - 1).get(0));
            sequentialCardMatch(cardPack_5, cardBucket, cardsSample_23456, 0, 1, 2, 3, cardBucket.size() - 1);
        }

        //同花
        //先把牌组复制一份，按照花色排序
        ArrayList<Card> handCardsSuit = new ArrayList<>();
        Collections.sort(handCardsSuit, new Comparator<Card>() {
            @Override
            public int compare(Card card, Card t1) {
                if (card.getSuit() > t1.getSuit())
                    return 1;
                if (card.getSuit() == t1.getSuit())
                    return 0;
                return -1;
            }
        });
        ArrayList<ArrayList<Card>> cardPack_suit = new ArrayList<>();
        for (int i = 0; i < handCardsSuit.size(); ) {
            ArrayList<Card> cards = new ArrayList<>();
            cards.add(handCardsSuit.get(i));
            i++;
            while (i < handCardsSuit.size()) {
                if (handCardsSuit.get(i).getSuit() == handCardsSuit.get(i + 1).getSuit()) {
                    cards.add(handCardsSuit.get(i + 1));
                    i++;
                } else
                    break;
            }
            //把能组成同花的其余权重降低，但是同花只储存到同花数组
            if (cards.size() >= 5) {
                for (int i1 = 0; i1 < cards.size(); i1++) {
                    for (int j = 0; j < 5; j++) {
                        cards.get(j).weights[j] *= decreaseRate_small;
                    }
                }
                cardPack_suit.add(cards);
            }
        }
        //葫芦
        //葫芦取cardPack_3和cardPack_2中不一样点数的进行排列组合, 由于葫芦是看3张牌的最大张大小, 因此2张牌的部分越小越有利
        //所以3张牌的3张出牌权重直接降低，2张牌的5张出牌权重按照点数从小到大越逐渐降低。
        for (int i = 0; i < cardPack_3.size(); i++) {
            ArrayList<Card> cards_3 = new ArrayList<>();
            cards_3.addAll(cardPack_3.get(i));
            for (int i1 = 0; i1 < cardPack_2.size(); i1++) {
                if (cardPack_2.get(i1).get(0).getFigure() != cards_3.get(0).getFigure()) {
                    ArrayList<Card> cards_2 = cardPack_2.get(i1);
                    for (int i2 = 0; i2 < cards_2.size(); i2++) {
                        //权重加上对应逆序号乘递减值, 比如倒数第一张牌的权重要加上一个递减值
                        cards_2.get(i2).weights[4] -= decreaseGap * i1;
                    }
                    ArrayList<Card> cards = new ArrayList<>();
                    cards.addAll(cards_2);
                    cards.addAll(cards_3);
                    for (int i2 = 0; i2 < cards.size(); i2++) {
                        cards.get(i2).weights[1] *= decreaseRate_small;
                        cards.get(i2).weights[2] *= decreaseRate_small;
                    }
                    cardPack_5.add(cards);
                }
            }
        }
        //金刚
        //金刚最好判, 因为只要四带一，任意一张都可以拼
        //但是, 其中的5张出牌权重要根据牌从大到小递增, 并且要根据牌的1张出牌权重从大到小递减
        for (int i = 0; i < cardPack_4.size(); i++) {
            for (int i1 = 0; i1 < handCards.size(); i1++) {
                if (handCards.get(i1).getFigure() != cardPack_4.get(i).get(0).getFigure()) {
                    Card card = handCards.get(i1);
                    ArrayList<Card> cards_4 = cardPack_4.get(i);
                    card.weights[4] += decreaseGap * (handCards.size() - i1);
                    card.weights[4] += decreaseGap * card.weights[0];
                    for (int i2 = 0; i2 < cards_4.size(); i2++) {
                        cards_4.get(i2).weights[3] *= decreaseGap;
                    }
                    ArrayList<Card> cards = new ArrayList<>();
                    cards.add(card);
                    cards.addAll(cards_4);
                    cardPack_5.add(cards);
                }
            }
        }
        //在这里进行权重的最终调整
        //五张牌不允许打不过
        for (int i = 0; i < cardPack_5.size(); i++) {
            ArrayList<Card> cards = cardPack_5.get(i);
            if (!CardSystem.getInstance().judgeCards(cards)) {
                for (int i1 = 0; i1 < cards.size(); i1++) {
                    cards.get(i1).weights[4] = 0;
                }
            }
        }
        //保持牌的平衡
        ArrayList<Card> smallPack = new ArrayList<>();
        ArrayList<Card> bigPack = new ArrayList<>();
        for (Card handCard : handCards) {
            if (handCard.getFigure() <= Card.TEN) {
                smallPack.add(handCard);
            } else {
                bigPack.add(handCard);
            }
        }
        int differ = smallPack.size() - bigPack.size();
        if (differ < - 2) {
            for (Card card : smallPack) {
                for (int i = 0; i < 5; i++) {
                    card.weights[i] *= decreaseRate_big;
                }
            }
            for (int i = 0; i < bigPack.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    bigPack.get(i).weights[j] -= i * decreaseGap * 2;
                    bigPack.get(i).weights[j] /= decreaseRate_small;
                }
            }
        } else {
            for (Card card : smallPack) {
                for (int i = 0; i < 5; i++) {
                    card.weights[i] /= decreaseRate_small;
                }
            }
            for (int i = 0; i < bigPack.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    bigPack.get(i).weights[j] *= (decreaseRate_big + decreaseRate_small) / 2;
                    bigPack.get(i).weights[j] -= (float) differ * decreaseGap;
                    bigPack.get(i).weights[j] -= ((float) i / (float)bigPack.size()) * decreaseGap / 2;
                }

            }
        }

        //顶大
        if (CardSystem.getInstance().getCardNums((manager.getId() + 1) % 4) <= threshold_MAX) {
            for (int i = 0; i < handCards.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    handCards.get(i).weights[j] += i * decreaseGap;
                }
            }
        }
        //顶3家
        if (minCardNum <= threshold_MAX) {
            for (int i = 0; i < handCards.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    handCards.get(i).weights[j] += i * decreaseGap / 2;
                }
            }
        }
        if (CardSystem.getInstance().getTurnAmount() == 0) {
            //阶砖3权重无敌
            Collections.sort(handCards);
            for (int i = 0; i < 5; i++) {
                handCards.get(0).weights[i] = 100000;
            }
        }
        //
        if (CardSystem.getInstance().getTurnAmount() == 0 || CardSystem.getInstance().getLastPlayerID() == manager.getId()) {
            threshold_used = -10000;
            lastCardSize = 1;
            if (cardPack_2.size() > 0) {
                if (CardSystem.getInstance().getTurnAmount() == 0)
                    for (ArrayList<Card> cards : cardPack_2) {
                        if (CardSystem.getInstance().judgeCards(cards)) {
                            lastCardSize = 2;
                        }
                    }
                else
                    lastCardSize = 2;
            }
            if (cardPack_3.size() > 0) {
                if (CardSystem.getInstance().getTurnAmount() == 0)
                    for (ArrayList<Card> cards : cardPack_3) {
                        if (CardSystem.getInstance().judgeCards(cards)) {
                            lastCardSize = 3;
                        }
                    }
                else
                    lastCardSize = 3;
            }
            if (cardPack_4.size() > 0) {
                if (CardSystem.getInstance().getTurnAmount() == 0)
                    for (ArrayList<Card> cards : cardPack_4) {
                        if (CardSystem.getInstance().judgeCards(cards)) {
                            lastCardSize = 4;
                        }
                    }
                else
                    lastCardSize = 4;
            }
            if (cardPack_5.size() > 0 || cardPack_suit.size() > 0) {
                if (CardSystem.getInstance().getTurnAmount() == 0) {
                    for (ArrayList<Card> cards : cardPack_5) {
                        if (CardSystem.getInstance().judgeCards(cards)) {
                            lastCardSize = 5;
                        }
                    }
                } else
                    lastCardSize = 5;
            }
            if (smallPack.size() > bigPack.size() && cardPack_5.size() <= 1) {
                lastCardSize = 1;
            }
        }
        int i = lastCardSize;
        switch (i) {
            case 1:
                if (chooseOneCard(cardPack_1) != null) {
                    manager.addChosenCard(chooseOneCard(cardPack_1).get(0));
                    return true;
                }
                break;
            case 2:
                ArrayList<Card> cards_2 = chooseTwoCards(cardPack_2);
                if (cards_2 != null) {
                    manager.addChosenCard(cards_2.get(0));
                    manager.addChosenCard(cards_2.get(1));
                    return true;
                }
                break;
            case 3:
                ArrayList<Card> cards_3 = chooseThreeCards(cardPack_3);
                if (cards_3 != null) {
                    manager.addChosenCard(cards_3.get(0));
                    manager.addChosenCard(cards_3.get(1));
                    manager.addChosenCard(cards_3.get(2));
                    return true;
                }
                break;
            case 4:
                ArrayList<Card> cards_4 = chooseFourCards(cardPack_4);
                if (cards_4 != null) {
                    manager.addChosenCard(cards_4.get(0));
                    manager.addChosenCard(cards_4.get(1));
                    manager.addChosenCard(cards_4.get(2));
                    manager.addChosenCard(cards_4.get(3));
                    return true;
                }
                break;
            case 5:
                ArrayList<Card> cards_5 = chooseFiveCards(cardPack_5);
                if (cards_5 != null) {
                    manager.addChosenCard(cards_5.get(0));
                    manager.addChosenCard(cards_5.get(1));
                    manager.addChosenCard(cards_5.get(2));
                    manager.addChosenCard(cards_5.get(3));
                    manager.addChosenCard(cards_5.get(4));
                    return true;
                }
                cards_5 = chooseFiveSuitCards(cardPack_suit);
                if (cards_5 != null) {
                    manager.addChosenCard(cards_5.get(0));
                    manager.addChosenCard(cards_5.get(1));
                    manager.addChosenCard(cards_5.get(2));
                    manager.addChosenCard(cards_5.get(3));
                    manager.addChosenCard(cards_5.get(4));
                    return true;
                }
                return false;
            default:

        }
        return false;
    }

    private void sequentialCardMatch(ArrayList<ArrayList<Card>> cardPack_5, ArrayList<ArrayList<Card>> cardBucket, ArrayList<Card> cardsSample, int... i) {
        int cardType = CardSystem.getInstance().judgeCardType(cardsSample);
        if (cardType == 0 || cardType == 4) {//判定为顺子
            for (int i1 = 0; i1 < cardBucket.get(i[0]).size(); i1++) {
                //第一层循环, 放入第一张
                Card card_1 = cardBucket.get(i[0]).get(i1);
                for (int i2 = 0; i2 < cardBucket.get(i[1]).size(); i2++) {
                    //第二层循环, 放入第二张
                    Card card_2 = cardBucket.get(i[1]).get(i2);
                    for (int i3 = 0; i3 < cardBucket.get(i[2]).size(); i3++) {
                        //第三层循环, 放入第三张
                        Card card_3 = cardBucket.get(i[2]).get(i3);
                        for (int i4 = 0; i4 < cardBucket.get(i[3]).size(); i4++) {
                            //第四层循环, 放入第四张
                            Card card_4 = cardBucket.get(i[3]).get(i4);
                            for (int i5 = 0; i5 < cardBucket.get(i[4]).size(); i5++) {
                                //第五层循环, 放入第五张
                                Card card_5 = cardBucket.get(i[4]).get(i5);
                                //把以上数组合并
                                ArrayList<Card> cards = new ArrayList<>();
                                cards.add(card_1);
                                cards.add(card_2);
                                cards.add(card_3);
                                cards.add(card_4);
                                cards.add(card_5);
                                //降低它们的1到4权重
                                for (int k = 0; k < cards.size(); k++) {
                                    for (int k1 = 0; k1 < cards.get(k).weights.length - 1; k1++) {
                                        cards.get(k).weights[k1] *= decreaseRate_small;
                                    }
                                    //顺子可拆
                                    cards.get(k).weights[2] /= cards.get(k).weights[3];
                                    cards.get(k).weights[1] /= cards.get(k).weights[2];
                                    cards.get(k).weights[0] /= cards.get(k).weights[1];
                                }
                                cardPack_5.add(cards);
                                //若为同花顺，则升高这五张牌的五张出牌权重
                                if (CardSystem.getInstance().judgeCardType(cards) == 4) {
                                    for (int k = 0; k < cards.size(); k++) {
                                        cards.get(k).weights[4] /= decreaseRate_small;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Card> chooseOneCard(ArrayList<ArrayList<Card>> cardPack_1) {
        if (cardPack_1.size() > 0) {
            Collections.sort(cardPack_1, new Comparator<ArrayList<Card>>() {
                @Override
                public int compare(ArrayList<Card> cards, ArrayList<Card> t1) {
                    if (cards.get(0).weights[0] > t1.get(0).weights[0]) {
                        return -1;
                    }
                    if (cards.get(0).weights[0] == t1.get(0).weights[0]) {
                        return 0;
                    }
                    return 1;
                }
            });
            ArrayList<Card> cards = cardPack_1.get(0);
            if (CardSystem.getInstance().getTurnAmount() == 0 || CardSystem.getInstance().getLastPlayerID() == manager.getId()) {
                for (int i = 0; i < cardPack_1.size() - 1; i++) {
                    if (CardSystem.getInstance().judgeCards(cards)) {
                        break;
                    } else {
                        cards = cardPack_1.get(i + 1);
                    }
                }
            }
            if (CardSystem.getInstance().judgeCards(cards)) {
                if (cards.get(0).weights[0] > threshold_used)
                    return cards;
            }
        }
        return null;
    }

    private ArrayList<Card> chooseTwoCards(ArrayList<ArrayList<Card>> cardPack_2) {
        if (cardPack_2.size() > 0) {
            Collections.sort(cardPack_2, new Comparator<ArrayList<Card>>() {
                @Override
                public int compare(ArrayList<Card> cards, ArrayList<Card> t1) {
                    float a = cards.get(0).weights[1] + cards.get(1).weights[1];
                    float b = t1.get(0).weights[1] + t1.get(1).weights[1];
                    if (a > b) {
                        return -1;
                    }
                    if (a == b) {
                        return 0;
                    }
                    return 1;
                }
            });
            ArrayList<Card> cards = cardPack_2.get(0);
            if (CardSystem.getInstance().getTurnAmount() == 0 || CardSystem.getInstance().getLastPlayerID() == manager.getId()) {
                for (int i = 0; i < cardPack_2.size() - 1; i++) {
                    if (CardSystem.getInstance().judgeCards(cards)) {
                        break;
                    } else {
                        cards = cardPack_2.get(i + 1);
                    }
                }
            }
            if (CardSystem.getInstance().judgeCards(cards)) {
                float weight = 0;
                for (int i = 0; i < cards.size(); i++) {
                    weight += cards.get(i).weights[1];
                }
                weight /= 2;
                if (weight > threshold_used)
                    return cards;
            }
        }
        return null;
    }

    private ArrayList<Card> chooseThreeCards(ArrayList<ArrayList<Card>> cardPack_3) {
        if (cardPack_3.size() > 0) {
            Collections.sort(cardPack_3, new Comparator<ArrayList<Card>>() {
                @Override
                public int compare(ArrayList<Card> cards, ArrayList<Card> t1) {
                    float a = cards.get(0).weights[2] + cards.get(1).weights[2] + cards.get(2).weights[2];
                    float b = t1.get(0).weights[2] + t1.get(1).weights[2] + t1.get(2).weights[2];
                    if (a > b) {
                        return -1;
                    }
                    if (a == b) {
                        return 0;
                    }
                    return 1;
                }
            });
            ArrayList<Card> cards = cardPack_3.get(0);
            if (CardSystem.getInstance().getTurnAmount() == 0 || CardSystem.getInstance().getLastPlayerID() == manager.getId()) {
                for (int i = 0; i < cardPack_3.size() - 1; i++) {
                    if (CardSystem.getInstance().judgeCards(cards)) {
                        break;
                    } else {
                        cards = cardPack_3.get(i + 1);
                    }
                }
            }
            if (CardSystem.getInstance().judgeCards(cards)) {
                float weight = 0;
                for (int i = 0; i < cards.size(); i++) {
                    weight += cards.get(i).weights[2];
                }
                weight /= 3;
                if (weight > threshold_used)
                    return cards;
            }
        }
        return null;
    }

    private ArrayList<Card> chooseFourCards(ArrayList<ArrayList<Card>> cardPack_4) {
        if (cardPack_4.size() > 0) {
            Collections.sort(cardPack_4, new Comparator<ArrayList<Card>>() {
                @Override
                public int compare(ArrayList<Card> cards, ArrayList<Card> t1) {
                    float a = cards.get(0).weights[3] + cards.get(1).weights[3] + cards.get(2).weights[3] + cards.get(3).weights[3];
                    float b = t1.get(0).weights[3] + t1.get(1).weights[3] + t1.get(2).weights[3] + t1.get(3).weights[3];
                    if (a > b) {
                        return -1;
                    }
                    if (a == b) {
                        return 0;
                    }
                    return 1;
                }
            });
            ArrayList<Card> cards = cardPack_4.get(0);
            if (CardSystem.getInstance().getTurnAmount() == 0 || CardSystem.getInstance().getLastPlayerID() == manager.getId()) {
                for (int i = 0; i < cardPack_4.size() - 1; i++) {
                    if (CardSystem.getInstance().judgeCards(cards)) {
                        break;
                    } else {
                        cards = cardPack_4.get(i + 1);
                    }
                }
            }
            if (CardSystem.getInstance().judgeCards(cards)) {
                float weight = 0;
                for (int i = 0; i < cards.size(); i++) {
                    weight += cards.get(i).weights[3];
                }
                weight /= 4;
                if (weight > threshold_used)
                    return cards;
            }
        }
        return null;
    }

    private ArrayList<Card> chooseFiveCards(ArrayList<ArrayList<Card>> cardPack_5) {
        if (cardPack_5.size() > 0) {
            Collections.sort(cardPack_5, new Comparator<ArrayList<Card>>() {
                @Override
                public int compare(ArrayList<Card> cards, ArrayList<Card> t1) {
                    float a = cards.get(0).weights[4] + cards.get(1).weights[4] + cards.get(2).weights[4] + cards.get(3).weights[4] + cards.get(4).weights[4];
                    float b = t1.get(0).weights[4] + t1.get(1).weights[4] + t1.get(2).weights[4] + t1.get(3).weights[4] + t1.get(4).weights[4];
                    if (a > b) {
                        return -1;
                    }
                    if (a == b) {
                        return 0;
                    }
                    return 1;
                }
            });
            ArrayList<Card> cards;
            cards = cardPack_5.get(0);
            if (CardSystem.getInstance().getTurnAmount() == 0 || CardSystem.getInstance().getLastPlayerID() == manager.getId()) {
                for (int i = 0; i < cardPack_5.size() - 1; i++) {
                    if (CardSystem.getInstance().judgeCards(cards)) {
                        break;
                    } else {
                        cards = cardPack_5.get(i + 1);
                    }
                }
            }
            if (CardSystem.getInstance().judgeCards(cards)) {
                float weight = 0;
                for (int i = 0; i < cards.size(); i++) {
                    weight += cards.get(i).weights[4];
                }
                weight /= 5;
                if (weight > threshold_used)
                    return cards;
            }
        }
        return null;
    }

    private ArrayList<Card> chooseFiveSuitCards(ArrayList<ArrayList<Card>> cardPack_suit) {
        if (cardPack_suit.size() > 0) {
            ArrayList<Card> cards = new ArrayList<>();
            for (int i = 0; i < cardPack_suit.size(); i++) {
                Collections.sort(cardPack_suit.get(i));
            }
            for (int i = 0; i < 5; i++) {
                cards.add(cardPack_suit.get(0).get(i));
            }
            if (CardSystem.getInstance().getTurnAmount() == 0 || CardSystem.getInstance().getLastPlayerID() == manager.getId()) {
                for (int i = 0; i < cardPack_suit.size(); i++) {
                    if (CardSystem.getInstance().judgeCards(cards)) {
                        break;
                    } else {
                        for (int j = 0; j < cardPack_suit.get(i).size() - 4; j++) {
                            if (CardSystem.getInstance().judgeCards(cards)) {
                                break;
                            } else {
                                cards.clear();
                                for (int k = j; k < j + 5; k++) {
                                    cards.add(cardPack_suit.get(i).get(k));
                                }
                            }
                        }
                    }
                }
            }
            if (CardSystem.getInstance().judgeCards(cards)) {
                float weight = 0;
                for (int i = 0; i < 5; i++) {
                    weight += cardPack_suit.get(0).get(i).weights[4];
                }
                weight /= 5;
                if (weight > threshold_used)
                    return cards;
            }
        }
        return null;
    }
}




