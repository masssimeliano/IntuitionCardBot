package com.masssimeliano.intuitioncardbot.telegram.core;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

public final class BotKeyboard {

    private static InlineKeyboardButton button(String text, String data) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(data)
                .build();
    }

    private static InlineKeyboardRow row(InlineKeyboardButton... buttons) {
        return new InlineKeyboardRow(buttons);
    }

    private static InlineKeyboardMarkup keyboard(InlineKeyboardRow... rows) {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(rows))
                .build();
    }

    public static InlineKeyboardMarkup mainMenu() {
        return keyboard(
                row(button("🎴 Розпочати игру", "nav:modes")),
                row(button("📊 Статистика", "nav:stats")),
                row(button("ℹ️ Про бота", "nav:about")));
    }

    public static InlineKeyboardMarkup modesMenu() {
        return keyboard(
                row(button("🔴⚫ Колір", "mode:COLOR")),
                row(button("♠️♥️♦️♣️ Масть", "mode:SUIT")),
                row(button("🔢 Номінал", "mode:RANK")),
                row(button("🃏 Повна карта", "mode:FULL")),
                row(button("⬅️ Назад", "nav:main")));
    }

    public static InlineKeyboardMarkup colorPick() {
        return keyboard(
                row(
                        button("🔴 Червоний", "pick:COLOR:RED"),
                        button("⚫ Чорний", "pick:COLOR:BLACK")),
                row(button("⬅️ В меню", "nav:modes")));
    }

    public static InlineKeyboardMarkup suitPick() {
        return keyboard(
                row(
                        button("♠️ Піки", "pick:SUIT:SPADES"),
                        button("♥️ Черви", "pick:SUIT:HEARTS")),
                row(button("♦️ Бубни", "pick:SUIT:DIAMONDS"),
                        button("♣️ Трефи", "pick:SUIT:CLUBS")),
                row(button("⬅️ В меню", "nav:modes")));
    }

    public static InlineKeyboardMarkup rankPick(String prefix) {
        // prefix: "pick:RANK:" or "pick:FULL:RANK:"
        return keyboard(
                row(
                        button("A", prefix + "A"),
                        button("K", prefix + "K"),
                        button("Q", prefix + "Q"),
                        button("J", prefix + "J")),
                row(
                        button("10", prefix + "10"),
                        button("9", prefix + "9"),
                        button("8", prefix + "8")),
                row(
                        button("7", prefix + "7"),
                        button("6", prefix + "6"),
                        button("5", prefix + "5")),
                row(
                        button("4", prefix + "4"),
                        button("3", prefix + "3"),
                        button("2", prefix + "2")),
                row(button("⬅️ В меню", "nav:modes")));
    }

    public static InlineKeyboardMarkup fullPick() {
        return keyboard(
                row(
                        button("♠️", "pick:FULL:SUIT:SPADES"),
                        button("♥️", "pick:FULL:SUIT:HEARTS"),
                        button("♦️", "pick:FULL:SUIT:DIAMONDS"),
                        button("♣️", "pick:FULL:SUIT:CLUBS")),
                row(button("⬅️ Назад до номіналу", "nav:full_rank")),
                row(button("🏠 В меню", "nav:modes")));
    }

    public static InlineKeyboardMarkup afterRound() {
        return keyboard(
                row(button("🔁 Ще раз", "again")),
                row(button("🎴 Змінити режим", "nav:modes")),
                row(button("📊 Моя статистика", "nav:stats")));
    }

    public static InlineKeyboardMarkup statsMenu() {
        return keyboard(
                row(button("📈 За весь час", "stats:TOTAL")),
                row(
                        button("📅 Сьогодні", "stats:TODAY"),
                        button("🕐 Вчора", "stats:YESTERDAY")),
                row(
                        button("⏳ Тиждень", "stats:WEEK"),
                        button("⏰ Місяць", "stats:MONTH")),
                row(button("⬅️ Назад", "nav:main")));
    }
}
