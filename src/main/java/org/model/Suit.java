package org.model;

public enum Suit {
    CLUB("♣", 1),      // 梅花
    DIAMOND("♦", 2),   // 方塊
    HEART("♥", 3),     // 紅心
    SPADE("♠", 4);     // 黑桃

    private final String symbol;
    private final int value;

    Suit(String symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }
}
