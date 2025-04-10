package org;

import org.game.Big2;
import org.pattern.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("歡迎來到大老二卡牌遊戲！");
        System.out.println("=========================");

        // 創建責任鏈
        CardPattern patternChain;
        CardPattern fullHouse = new FullHouse();
        CardPattern straight = new Straight();
        CardPattern pair = new Pair();
        CardPattern single = new Single();

        fullHouse.setNext(straight);
        straight.setNext(pair);
        pair.setNext(single);

        patternChain = fullHouse;

        // 創建遊戲實例
        Big2 game = new Big2(patternChain);

        // 創建玩家
        System.out.println("\n【玩家創建階段】");
        System.out.println("請依次輸入四位玩家的名稱，輸入 'AI' 代表電腦玩家");
        game.createPlayers();

        // 發牌
        System.out.println("\n【發牌階段】");
        System.out.println("正在洗牌並發牌...");
        game.dealCards();

        // 開始遊戲
        System.out.println("\n【遊戲開始】");
        System.out.println("遊戲規則：");
        System.out.println("1. 持有梅花3的玩家先出牌");
        System.out.println("2. 必須出比上一家更大的牌");
        System.out.println("3. 連續三家跳過後，牌桌重置");
        System.out.println("4. 首先出完所有手牌的玩家獲勝");
        System.out.println("=========================");

        // 開始遊戲主循環
        game.gameStart();

        // 詢問是否再玩一局
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n是否要再玩一局？(y/n)");
        String answer = scanner.nextLine().trim().toLowerCase();

        if ("y".equals(answer)) {
            main(args); // 遞迴調用，重新開始遊戲
        } else {
            System.out.println("感謝遊玩，下次再見！");
        }
    }
}
