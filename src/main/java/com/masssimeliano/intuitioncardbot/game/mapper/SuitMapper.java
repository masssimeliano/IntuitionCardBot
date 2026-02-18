package com.masssimeliano.intuitioncardbot.game.mapper;

public class SuitMapper {

    public static String mapToText(String emoji) {
        if ("♣️".equals(emoji)) {
            return "CLUBS";
        }
        if ("♥️".equals(emoji)) {
            return "HEARTS";
        }
        if ("♦️".equals(emoji)) {
            return "DIAMONDS";
        }
        return "♠️";
    }

    public static String mapToEmoji(String text) {
        if ("SPADES".equals(text)) {
            return "♠️";
        }
        if ("HEARTS".equals(text)) {
            return "♥️";
        }
        if ("DIAMONDS".equals(text)) {
            return "♦️";
        }
        return "♣️";
    }
}
