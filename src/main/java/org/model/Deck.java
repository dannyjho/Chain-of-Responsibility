package org.model;

import org.model.Card;
import org.model.Rank;
import org.model.Suit;
import org.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    List<Card> deck = new ArrayList<>();

    public Deck() {
        this.deck = createDeck();
    }

    public List<Card> getDeck() {
        return deck;
    }

    private List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public void deal(List<Player> players){
        Card clubThree = new Card(Suit.CLUB, Rank.THREE);
        int clubThreeIndex = -1;

        // 發牌
        for (int i = 0; i < deck.size(); i++) {
            Card card = deck.get(i);
            int playerIndex = i % 4;
            players.get(playerIndex).addHandCard(card);

            // 為所有玩家排序手牌
            for (Player player : players) {
                player.sortHandCards();
            }

            // 檢查是否為梅花3
            if (card.equals(clubThree)) {
                clubThreeIndex = playerIndex;
            }
        }

        // 設置持有梅花3的玩家為先手
        if (clubThreeIndex != -1) {
            players.get(clubThreeIndex).setHasClubThree(true);
        }
    }
}
