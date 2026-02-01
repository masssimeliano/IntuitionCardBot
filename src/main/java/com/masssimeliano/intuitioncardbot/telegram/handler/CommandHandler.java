package com.masssimeliano.intuitioncardbot.telegram.handler;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface CommandHandler {

    void handle(Message updateMessage);
}
