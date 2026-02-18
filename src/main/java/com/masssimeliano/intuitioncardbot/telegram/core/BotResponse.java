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
        String ACTION_CHOICE = "👋 Обери дію нижче";
        String MODE_CHOICE = "🎴 Обери режим гри. Чим складніше - тим цікавіше 😉";
        String STATISTICS_CHOICE = "📊 Обери період статистики";
        String ABOUT = "ℹ️ Про бота:\n\n\uD83C\uDCCF Бот для тренування інтуїції ✨\n" +
                "\n" +
                "Простий та захопливий Telegram-бот, де ти перевіряєш свою інтуїцію за допомогою колоди з 52 карт.\n" +
                "\n" +
                "\uD83C\uDFAE Режими гри:\n" +
                "- \uD83D\uDD34⚫\uFE0F вгадати колір карти\n" +
                "- ♠\uFE0F♥\uFE0F♦\uFE0F♣\uFE0F вгадати масть\n" +
                "- \uD83D\uDD22 вгадати номінал карти (без масті)\n" +
                "- \uD83C\uDCCF вгадати повну карту\n" +
                "\n" +
                "\uD83D\uDCCA Статистика:\n" +
                "- кількість спроб і вдалих відповідей\n" +
                "- відсоток точності\n" +
                "- прогрес за сьогодні, вчора, тиждень, місяць, пів року та рік\n" +
                "\n" +
                "Ніякого зайвого - тільки ти, карти та твоя інтуїція. Грай у будь-який момент і спостерігай, як вона прокачується \uD83D\uDE80";
        String UNKNOWN_ERROR = "Помилка, спробуйте обрати іншу опцію...";
    }

    interface Mode {
        String COLOR_CHOICE = "🔴⚫ Обери колір карти:";
        String SUIT_CHOICE = "♠️♥️♦️♣️ Обери масть карти:";
        String RANK_CHOICE = "🔢 Обери номінал карти:";
        String FULL_CHOICE = "🃏 Обери спочатку номінал, а потім масть:";
    }

    interface Pick {
        static String RESULT_COLOR(String userChoice, String actualColor) {
            boolean correct = actualColor.equals(userChoice);
            return String.format(
                    "🎴 Режим: Колір\n\nТвій вибір: %s\nКарта була: %s\n\n" + (correct ? CORRECT : WRONG),
                    userChoice,
                    actualColor
            );
        }

        static String RESULT_SUIT(String userChoice, String actualSuit) {
            boolean correct = actualSuit.equals(userChoice);
            return String.format(
                    "🎴 Режим: Масть\n\nТвій вибір: %s\nКарта була: %s\n\n" + (correct ? CORRECT : WRONG),
                    userChoice,
                    actualSuit
            );
        }

        static String RESULT_RANK(String userChoice, String actualRank) {
            boolean correct = actualRank.equals(userChoice);
            return String.format(
                    "🎴 Режим: Номінал\n\nТвій вибір: %s\nКарта була: %s\n\n" + (correct ? CORRECT : WRONG),
                    userChoice,
                    actualRank
            );
        }

        static String RESULT_FULL(String userChoice, String actualCard) {
            boolean correct = actualCard.equals(userChoice);
            return String.format(
                    "🃏 Режим: Повна карта\n\nТвій вибір: %s\nКарта була: %s\n\n" + (correct ? CORRECT : WRONG),
                    userChoice,
                    actualCard
            );
        }

        String FULL_AFTER_RANK = "🃏 Тепер обери масть карти:";
        String CORRECT = "✅ Вірно!";
        String WRONG = "❌ Невірно.";
    }

    interface Stats {
        static String RESULT_FULL(String timePeriod, int amountGamesPlayed, int amountCorrectGuesses) {
            double percentage = amountGamesPlayed == 0 ? 0.0 : (amountCorrectGuesses * 100.0) / amountGamesPlayed;
            return String.format(
                    "%s\n\n📈 Зіграно: %d\n✅ Відгадано: %d\n🃏 Корректність інтуіції: %.1f%%",
                    timePeriod,
                    amountGamesPlayed,
                    amountCorrectGuesses,
                    percentage
            );
        }
    }
}
