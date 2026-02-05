package com.masssimeliano.intuitioncardbot.telegram.handler.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {

    void handle(CallbackQuery callbackQuery);
}
