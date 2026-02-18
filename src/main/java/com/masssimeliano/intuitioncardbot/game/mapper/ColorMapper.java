package com.masssimeliano.intuitioncardbot.game.mapper;

public class ColorMapper {

    public static String mapToText(String emoji) {
        if ("🔴".equals(emoji)) {
            return "RED";
        }
        return "BLACK";
    }

    public static String mapToEmoji(String text) {
        if ("RED".equals(text)) {
            return "🔴";
        }
        return "⚫";
    }
}
