package org.player;

import org.model.Card;
import org.pattern.CardPattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HumanPlayer extends Player {

    private final Scanner scanner;

    public HumanPlayer(String name) {
        super(name);
        scanner = new Scanner(System.in);
    }

    @Override
    public List<Card> playCards(CardPattern topCardPattern) {
        List<Card> handCards = getHandCards();

        // 顯示當前手牌（帶索引）
        for (int i = 0; i < handCards.size(); i++) {
            System.out.printf("%-5d", i);
        }
        System.out.println();

        for (Card handCard : handCards) {
            System.out.printf("%-5s", handCard);
        }
        System.out.println();

        // 顯示頂牌資訊
        if (topCardPattern != null) {
            System.out.println("topCard：" + topCardPattern.getDescription() + " " +
                    topCardPattern.getCards().stream()
                            .map(Card::toString)
                            .collect(Collectors.joining(" ")));
        } else {
            System.out.println("當前無頂牌，你可以出任何有效的牌型");
        }

        // 提示玩家出牌或跳過
        System.out.println("請選擇要出的牌(輸入牌的索引，用空格分隔，或輸入 'pass' 跳過):");
        String input = scanner.nextLine().trim();

        // 處理跳過
        if ("pass".equalsIgnoreCase(input)) {
            return Collections.emptyList();
        }

        try {
            // 解析輸入
            String[] indices = input.split("\\s+");
            List<Card> selectedCards = new ArrayList<>();

            for (String indexStr : indices) {
                int index = Integer.parseInt(indexStr);
                if (index >= 0 && index < handCards.size()) {
                    selectedCards.add(handCards.get(index));
                } else {
                    System.out.println("無效的索引：" + index);
                    return playCards(topCardPattern); // 遞迴重試
                }
            }

            return selectedCards;

        } catch (NumberFormatException e) {
            System.out.println("輸入格式錯誤，請輸入數字索引或 'pass'");
            return playCards(topCardPattern); // 遞迴重試
        }
    }
}
