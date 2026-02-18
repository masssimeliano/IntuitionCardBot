package com.masssimeliano.intuitioncardbot.telegram.handler.command;

import com.masssimeliano.intuitioncardbot.telegram.core.BotMessage;
import com.masssimeliano.intuitioncardbot.telegram.core.BotResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class UnknownCommandHandler implements CommandHandler {

    private final TelegramClient telegramClient;

    public void handle(Message updateMessage) {
        long chatId = updateMessage.getChatId();

        String text = BotResponse.Error.UNKNOWN_TEXT_MESSAGE;

        BotMessage message = BotMessage.builder()
                .chatId(chatId)
                .telegramClient(telegramClient)
                .text(text)
                .build();

        message.send();
    }
}
