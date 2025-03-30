package org.pattern;

import org.model.Card;
import org.model.Rank;

import java.util.*;

public class Straight extends CardPattern {
    @Override
    public boolean isValid(List<Card> cards) {
        if (cards == null || cards.size() != 5) {
            return false;
        }

        // 檢查是否有重複點數
        Set<Rank> rankSet = new HashSet<>();
        for (Card card : cards) {
            rankSet.add(card.getRank());
        }

        // 如果有重複點數，不是順子
        if (rankSet.size() != 5) {
            return false;
        }

        // 排序
        List<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(Comparator.comparing(card -> card.getRank().getValue()));

        // 檢查是否為 A-2-3-4-5 這種特殊情況
        boolean isSpecialCase = isA2345Straight(sortedCards);
        if (isSpecialCase) {
            return true;
        }

        // 檢查是否連續
        for (int i = 0; i < sortedCards.size() - 1; i++) {
            if (sortedCards.get(i).getRank().getValue() + 1 !=
                    sortedCards.get(i + 1).getRank().getValue()) {
                return false;
            }
        }

        return true;
    }

    // 檢查是否為 A-2-3-4-5 這種特殊情況的順子
    private boolean isA2345Straight(List<Card> cards) {
        // 假設牌已按點數排序
        if (cards.size() != 5) return false;

        // 檢查是否有 A 和 2
        boolean hasAce = false;
        boolean hasTwo = false;
        for (Card card : cards) {
            if (card.getRank() == Rank.ACE) hasAce = true;
            if (card.getRank() == Rank.TWO) hasTwo = true;
        }

        // 如果同時有 A 和 2，檢查剩餘的牌是否為 3,4,5
        if (hasAce && hasTwo) {
            int count = 0;
            for (Card card : cards) {
                Rank rank = card.getRank();
                if (rank == Rank.THREE || rank == Rank.FOUR || rank == Rank.FIVE) {
                    count++;
                }
            }
            return count == 3;
        }

        return false;
    }

    @Override
    protected int calculateValue() {
        // 順子的值由最大的牌決定
        return cards.stream()
                .mapToInt(card -> card.getRank().getValue())
                .max()
                .orElse(0);
    }

    @Override
    public String getDescription() {
        return "順子";
    }

    @Override
    public int getPriority() {
        return 3; // 比對子高
    }
}
