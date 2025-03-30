package org.player;

import org.model.Card;
import org.pattern.CardPattern;
import org.model.Rank;
import org.model.Suit;

import java.util.*;

public class AIPlayer extends Player {
    private final CardPattern patternChain;

    public AIPlayer(String name, CardPattern patternChain) {
        super(name);
        this.patternChain = patternChain;
    }

    @Override
    public List<Card> playCards(CardPattern topCardPattern) {
        // 簡單的AI策略：找到能夠大過頂牌的最小牌組合
        // ... 策略邏輯 ...

        List<Card> handCards = getHandCards();

        for (Card handCard : handCards) {
            System.out.printf("%-6s", handCard);
        }

        System.out.println();

        // 如果是第一輪且擁有梅花3，必須出梅花3
        if (topCardPattern == null) {
            Card clubThree = new Card(Suit.CLUB, Rank.THREE);
            if (handCards.contains(clubThree)) {
                List<Card> play = new ArrayList<>();
                play.add(clubThree);
                return play;
            }
        }

        // 查找所有可出的牌型
        List<CardPattern> validPatterns = findValidPatterns(handCards, topCardPattern);

        if (validPatterns.isEmpty()) {
            return Collections.emptyList();
        }
        // 選擇最大的牌型
        CardPattern bestPattern = validPatterns.get(validPatterns.size() - 1);

        return bestPattern.getCards();
    }

    // 尋找所有可出的牌型
    private List<CardPattern> findValidPatterns(List<Card> handCards, CardPattern topCardPattern) {
        List<CardPattern> validPatterns = new ArrayList<>();

        // 嘗試所有可能的牌型組合
        List<List<Card>> allCombinations = generateCombinations(handCards);

        for (List<Card> combination : allCombinations) {
            CardPattern pattern = patternChain.handleRequest(combination);

            if (topCardPattern == null) {
                if (pattern != null) {
                    validPatterns.add(pattern);
                }
            } else {
                if (pattern != null && isValidPlay(pattern, topCardPattern)) {
                    validPatterns.add(pattern);
                }
            }
        }

        // 優化排序邏輯
        if (topCardPattern == null) {
            // 如果沒有頂牌，按照牌型優先級和大小排序
            validPatterns.sort((p1, p2) -> {
                if (p1.getPriority() != p2.getPriority()) {
                    return Integer.compare(p1.getPriority(), p2.getPriority());
                }
                return Integer.compare(p1.calculateValue(), p2.calculateValue());
            });
        } else {
            // 如果有頂牌，只需按照大小排序（因為已經過濾為相同類型）
            validPatterns.sort(Comparator.comparing(CardPattern::calculateValue));
        }

        return validPatterns;
    }

    private static boolean isValidPlay(CardPattern pattern, CardPattern topCardPattern) {
        return pattern.getClass() == topCardPattern.getClass() && pattern.isGreaterThan(topCardPattern);
    }

    // 生成所有可能的牌型組合
    private List<List<Card>> generateCombinations(List<Card> cards) {
        List<List<Card>> result = new ArrayList<>();

        // 單張
        for (Card card : cards) {
            result.add(Collections.singletonList(card));
        }

        // 對子（嘗試所有可能的2張牌組合）
        for (int i = 0; i < cards.size(); i++) {
            for (int j = i + 1; j < cards.size(); j++) {
                List<Card> pair = Arrays.asList(cards.get(i), cards.get(j));
                result.add(pair);
            }
        }

        // 五張牌組合（葫蘆和順子）
        generateFiveCardCombinations(cards, new ArrayList<>(), 0, 5, result);

        return result;
    }

    // 生成所有可能的五張牌組合
    private void generateFiveCardCombinations(List<Card> cards, List<Card> current, int start, int remain, List<List<Card>> result) {
        if (remain == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i <= cards.size() - remain; i++) {
            current.add(cards.get(i));
            generateFiveCardCombinations(cards, current, i + 1, remain - 1, result);
            current.remove(current.size() - 1);
        }
    }
}
