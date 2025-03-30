package org.pattern;


import org.model.Card;

import java.util.ArrayList;
import java.util.List;

public abstract class CardPattern {
    protected List<Card> cards;
    protected CardPattern next;

    // 檢查是否為有效的牌型
    public abstract boolean isValid(List<Card> cards);

    // 計算牌型的大小值，用於同類型比較
    public abstract int calculateValue();

    // 獲取牌型名稱/描述
    public abstract String getDescription();

    // 獲取牌型優先級
    public abstract int getPriority();

    // 設置下一個處理器
    public void setNext(CardPattern nextPattern) {
        this.next = nextPattern;
    }

    // 比較與另一個牌型的大小
    public boolean isGreaterThan(CardPattern other) {
        // 不同類型，比較優先級
        if (this.getClass() != other.getClass()) {
            return false;
        }
        // 相同類型，比較牌值
        return this.calculateValue() > other.calculateValue();
    }

    // 設置此牌型的牌
    public void setCards(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    // 獲取此牌型的牌
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    // 責任鏈處理請求
    public CardPattern handleRequest(List<Card> cards) {
        if (isValid(cards)) {
            try {
                // 創建當前類型的實例
                CardPattern result = this.getClass().getDeclaredConstructor().newInstance();
                result.setCards(cards);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (next != null) {
            // 傳遞給下一個處理器
            return next.handleRequest(cards);
        }
        // 沒有處理器能處理這組牌
        return null;
    }
}
