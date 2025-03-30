package org.pattern;

import org.model.Card;

import java.util.List;

public class Pair extends CardPattern {
    @Override
    public boolean isValid(List<Card> cards) {
        if (cards == null || cards.size() != 2) {
            return false;
        }

        // 檢查兩張牌是否點數相同
        return cards.get(0).getRank() == cards.get(1).getRank();
    }

    @Override
    protected int calculateValue() {
        int maxValue = 0;
        for (Card card : cards) {
            maxValue += card.getRank().getValue() * 100 + card.getSuit().getValue();
        }

        // 對子的值就是對子的點數
        return maxValue;
    }

    @Override
    public String getDescription() {
        return "對子";
    }

    @Override
    public int getPriority() {
        return 2; // 比單張高
    }
}
