package com.masssimeliano.intuitioncardbot.telegram.handler;

import com.masssimeliano.intuitioncardbot.telegram.BotMessage;
import com.masssimeliano.intuitioncardbot.telegram.BotResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class StartCommandHandler implements CommandHandler {

    private final TelegramClient telegramClient;

    public void handle(Message updateMessage) {
        long chatId = updateMessage.getChatId();
        String firstName = updateMessage.getFrom().getFirstName();

        BotMessage message = BotMessage.builder()
                .chatId(chatId)
                .telegramClient(telegramClient)
                .build();

        message.setText(BotResponse.Command.GREETING(firstName));
        message.send();
        message.setText(BotResponse.Command.INSTRUCTION);
        message.send();
    }
}
