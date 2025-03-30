package org.pattern;

import org.model.Card;

import java.util.List;

public class Single extends CardPattern {
    @Override
    public boolean isValid(List<Card> cards) {
        return cards != null && cards.size() == 1;
    }

    @Override
    protected int calculateValue() {
        // 單張牌的值就是牌的點數
        return cards.get(0).getRank().getValue() * 100 + cards.get(0).getSuit().getValue();
    }

    @Override
    public String getDescription() {
        return "單張";
    }

    @Override
    public int getPriority() {
        return 1; // 最低優先級
    }
}
