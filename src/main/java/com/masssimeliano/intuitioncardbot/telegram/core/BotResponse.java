package com.masssimeliano.intuitioncardbot.telegram.core;

public interface BotResponse {

    interface Command {
        static String GREETING(String firstName) {
            return String.format(
                    "Привіт, %s! \uD83D\uDE03\nЯ - Інтуїція Карти Бот \uD83D\uDD2E і з моєю допомогою ти зможеш тренувати свою інтуїцію!",
                    firstName);
        }

        String ALREADY_REGISTERED = "Ви вже зареєстровані в нашому боті. ⚠\uFE0F";
        String NOT_REGISTERED = "Тепер ви зареєстровані в нашому боті! ✅";
    }

    interface Error {
        String UNKNOWN_TEXT_MESSAGE = "Не розумію тебе на жаль. \uD83D\uDE22\nБот приймає лише комади через / (наприклад, /start) або через кнопки. ▶\uFE0F";
    }

    interface Navigation {
        String ACTION_CHOICE = "👋 Обери дію нижче:";
        String MODE_CHOICE = "🎴 Обери режим гри. Чим складніше - тим цікавіше 😉";
        String STATISTICS_CHOICE = "📊 Обери період статистики:";
        String ABOUT = "ℹ️ Про бота...";
        String FULL_RANK_CHOICE = "🃏 Повна карта: спочатку обери номінал (A, K, Q, J, 10...2)";
        String UNKNOWN_ERROR = "Помилка, спробуйте обрати іншу опцію...";
    }

    interface Mode {
        String COLOR_CHOICE = "🔴⚫ Обери колір карти:";
        String SUIT_CHOICE = "♠️♥️♦️♣️ Обери масть карти:";
        String RANK_CHOICE = "🔢 Обери номінал карти:";
        String FULL_CHOICE = "🃏 Повна карта: обери спочатку номінал, а потім масть.";
    }

    interface Pick {
        static String RESULT_COLOR(String userChoice, String actualColor, boolean correct) {
            return String.format(
                    "🎴 Режим: Колір\n\nТвій вибір: %s\nКарта була:\n\n%s" +
                    userChoice,
                    actualColor,
                    correct ? CORRECT : WRONG
            );
        }

        static String RESULT_SUIT(String userChoice, String actualSuit, boolean correct) {
            return String.format(
                    "🎴 Режим: Масть\n\nТвій вибір: %s\nКарта була:\n\n%s" +
                    userChoice,
                    actualSuit,
                    correct ? CORRECT : WRONG
            );
        }

        static String RESULT_RANK(String userChoice, String actualRank, boolean correct) {
            return String.format(
                    "🎴 Режим: Номінал\n\nТвій вибір: %s\nКарта була:\n\n%s" +
                    userChoice,
                    actualRank,
                    correct ? CORRECT : WRONG
            );
        }

        static String RESULT_FULL(String userChoice, String actualCard, boolean correct) {
            return String.format(
                    "🃏 Режим: Повна карта\n\nТвій вибір: %s\nКарта була:\n\n%s",
                    userChoice,
                    actualCard,
                    correct ? CORRECT : WRONG
            );
        }

        String FULL_AFTER_RANK = "🃏 Тепер обери масть карти:";
        String CORRECT = "✅ Вірно!";
        String WRONG = "❌ Невірно.";
    }
}
