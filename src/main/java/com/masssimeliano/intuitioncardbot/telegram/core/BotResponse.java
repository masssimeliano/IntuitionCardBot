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
}
