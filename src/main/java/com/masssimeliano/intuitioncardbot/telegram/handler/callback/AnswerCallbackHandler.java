package com.masssimeliano.intuitioncardbot.telegram.handler.callback;

import com.masssimeliano.intuitioncardbot.telegram.core.BotCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnswerCallbackHandler implements CallbackHandler {

    private final TelegramClient telegramClient;

    public void handle(CallbackQuery callbackQuery) {
        String callbackQueryId = callbackQuery.getId();

        BotCallback callback = BotCallback.builder()
                .telegramClient(telegramClient)
                .build();

        callback.answer(callbackQueryId);
    }
}
