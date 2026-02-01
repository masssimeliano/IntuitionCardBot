package com.masssimeliano.intuitioncardbot.telegram.handler;

import com.masssimeliano.intuitioncardbot.telegram.BotMessage;
import com.masssimeliano.intuitioncardbot.telegram.BotResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnknownCommandHandler implements CommandHandler {

    private final TelegramClient telegramClient;

    public void handle(Message updateMessage) {
        long chatId = updateMessage.getChatId();

        BotMessage message = BotMessage.builder()
                .chatId(chatId)
                .telegramClient(telegramClient)
                .build();

        message.setText(BotResponse.Error.UNKNOWN_TEXT_MESSAGE);
        message.send();
    }
}
