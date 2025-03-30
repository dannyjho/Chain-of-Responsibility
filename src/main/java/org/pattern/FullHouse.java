package org.pattern;

import org.model.Card;
import org.model.Rank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FullHouse extends CardPattern {

    @Override
    public boolean isValid(List<Card> cards) {
        if (cards == null || cards.size() != 5) {
            return false;
        }

        // 計算每個點數的牌的數量
        Map<Rank, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(),
                    rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        // 葫蘆必須是 3+2 組合
        return rankCount.size() == 2 &&
                rankCount.containsValue(3) &&
                rankCount.containsValue(2);
    }

    @Override
    protected int calculateValue() {
        // 葫蘆的值由三張相同點數的牌決定
        Map<Rank, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank));

        int fullHouseValue = 0;

        for (Map.Entry<Rank, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 3) {
                fullHouseValue = entry.getKey().getValue();
            }
        }

        return fullHouseValue;
    }

    @Override
    public String getDescription() {
        return "葫蘆";
    }

    @Override
    public int getPriority() {
        return 4; // 最高優先級
    }
}
