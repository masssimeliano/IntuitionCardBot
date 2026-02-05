package com.masssimeliano.intuitioncardbot.telegram.core;

public interface BotResponse {

    interface Command {
        static String GREETING(String firstName) {
            return String.format(
                    "Привіт, %s!\nЯ - Інтуїція Карти Бот \uD83D\uDD2E і з моєю допомогою ти зможеш тренувати свою інтуїцію!",
                    firstName);
        }
        String ALREADY_REGISTERED = "Ви вже зареєстровані в нашому боті.";
        String NOT_REGISTERED = "Тепер ви зареєстровані в нашому боті!";
    }

    interface Error {
        String WRONG_UPDATE_TYPE = "Неправильний тип данних.\nБот приймає лише комади через / (наприклад, /start) або через кнопки.";
        String UNKNOWN_TEXT_MESSAGE = "Такої команди не існує.\nБот приймає лише комади через / (наприклад, /start) або через кнопки.";
    }
}
