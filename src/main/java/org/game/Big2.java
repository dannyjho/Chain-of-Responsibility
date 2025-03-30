package org.game;

import org.pattern.*;
import org.model.Card;
import org.model.Deck;
import org.player.AIPlayer;
import org.player.HumanPlayer;
import org.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Big2 {
    private final List<Player> players;
    private CardPattern topCardPattern;
    private int currentPlayerIndex;
    private int consecutivePassCount;
    private CardPattern patternChain; // 牌型責任鏈
    private boolean gameOver = false;

    public Big2() {
        players = new ArrayList<>(4);
        topCardPattern = null;
        currentPlayerIndex = 0;
        consecutivePassCount = 0;
        initializePatternChain();
    }

    // 初始化牌型責任鏈
    private void initializePatternChain() {
        CardPattern fullHouse = new FullHouse();
        CardPattern straight = new Straight();
        CardPattern pair = new Pair();
        CardPattern single = new Single();

        fullHouse.setNext(straight);
        straight.setNext(pair);
        pair.setNext(single);

        patternChain = fullHouse;
    }

    // 創建玩家（從CLI收集玩家資訊）
    public void createPlayers() {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 4; i++) {
            System.out.println("請輸入玩家 " + i + " 的名稱（輸入 'AI' 代表電腦玩家）：");
            String name = scanner.nextLine();

            if ("AI".equalsIgnoreCase(name)) {
                players.add(new AIPlayer("AI-" + i, patternChain));
            } else {
                players.add(new HumanPlayer(name));
            }
        }
    }

    // 發牌
    public void dealCards() {
        Deck deck = new Deck();
        deck.shuffle();
        deck.deal(players);
    }

    // 遊戲主循環
    public void gameStart() {
        currentPlayerIndex = findHasClubThreePlayer();

        while (!gameOver) {
            Player currentPlayer = players.get(currentPlayerIndex);

            if (topCardPattern == null) {
                System.out.println("新的回合開始了。");
            }

            System.out.println("輪到 " + currentPlayer.getName() + " 了");

            // 玩家出牌
            List<Card> playedCards = currentPlayer.playCards(topCardPattern);

            if (playedCards.isEmpty()) {
                // 玩家跳過
                System.out.println("玩家 " + currentPlayer.getName() + " PASS.");
                consecutivePassCount++;

                if (consecutivePassCount >= 3) {
                    // 連續三人跳過，清空頂牌
                    updateTopPlay(null);
                }
            } else {
                // 判斷牌型
                CardPattern pattern = patternChain.handleRequest(playedCards);

                if (topCardPattern != null) {
                    if (pattern.getClass() != topCardPattern.getClass()) {
                        System.out.println("必須出與頂牌相同類型的牌！請重試");
                        continue;
                    }

                    // 檢查是否大過頂牌
                    if (!pattern.isGreaterThan(topCardPattern)) {
                        System.out.println("出牌不夠大！請重試");
                        continue;
                    }
                }

                // 有效出牌
                System.out.println("玩家 "+ currentPlayer.getName() + " 打出了 " + pattern.getDescription() + " " + playedCards);
                currentPlayer.removeCards(playedCards);
                updateTopPlay(pattern);

                // 檢查是否獲勝
                if (currentPlayer.getHandCards().isEmpty()) {
                    announceWinner(currentPlayer);
                    break;
                }
            }

            // 更新下一位玩家
            currentPlayerIndex = nextPlayer();
        }

        System.out.println("遊戲結束！");
    }

    private int nextPlayer() {
        return (currentPlayerIndex + 1) % 4;
    }

    private void updateTopPlay(CardPattern pattern) {
        topCardPattern = pattern;
        consecutivePassCount = 0;
    }

    private void announceWinner(Player currentPlayer) {
        System.out.println("遊戲結束，遊戲的勝利者為 " + currentPlayer.getName());
        gameOver = true;
    }

    private int findHasClubThreePlayer() {
        int hasClubThreePlayerIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isHasClubThree()) {
                hasClubThreePlayerIndex = i;
            }
        }
        return hasClubThreePlayerIndex;
    }
}
