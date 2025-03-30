package org.player;

import org.model.Card;
import org.pattern.CardPattern;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Player {
    private final String name;
    private final List<Card> handCards;
    private boolean hasClubThree = false;

    public Player(String name) {
        this.name = name;
        this.handCards = new ArrayList<>();
    }

    public void setHasClubThree(boolean hasClubThree) {
        this.hasClubThree = hasClubThree;
    }

    public boolean isHasClubThree() {
        return hasClubThree;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHandCards() {
        return handCards;
    }


    public void sortHandCards() {
        // 先比較點數
        // 點數相同，比較花色
        handCards.sort(Comparator.comparingInt((Card card) -> card.getRank().getValue())
                .thenComparingInt(card -> card.getSuit().getValue()));
    }

    // 玩家出牌（抽象方法，由子類實現）
    public abstract List<Card> playCards(CardPattern topCardPattern);

    // 移除已出的牌
    public void removeCards(List<Card> cards) {
        handCards.removeAll(cards);
    }

    // 檢查是否有特定的牌（如梅花3）

    public void addHandCard(Card card) {
        handCards.add(card);
    }
}
