package com.masssimeliano.intuitioncardbot.telegram.handler.command;

import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface CommandHandler {

    void handle(Message updateMessage);
}
