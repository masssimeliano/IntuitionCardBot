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
public class BotCallback {

    @NonNull
    private final TelegramClient telegramClient;

    public void answer(String callbackQueryId) {
        AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .build();

        try {
            telegramClient.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error answering the callback: {}", e.getMessage());
        }
    }
}
