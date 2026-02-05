package com.masssimeliano.intuitioncardbot.telegram.core;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class BotCallback {

    private final String callbackQueryId;

    @NonNull
    private final TelegramClient telegramClient;

    public void send() {
        AnswerCallbackQuery answerQuery = AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .build();

        try {
            telegramClient.execute(answerQuery);
        } catch (TelegramApiException e) {
            log.error("Error sending the answer callback: {}", e.getMessage());
        }
    }
}
