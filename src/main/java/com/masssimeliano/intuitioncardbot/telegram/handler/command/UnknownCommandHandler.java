package com.masssimeliano.intuitioncardbot.telegram.handler.command;

import com.masssimeliano.intuitioncardbot.telegram.core.BotKeyboard;
import com.masssimeliano.intuitioncardbot.telegram.core.BotMessage;
import com.masssimeliano.intuitioncardbot.telegram.core.BotResponse;
import com.masssimeliano.intuitioncardbot.telegram.model.BotUser;
import com.masssimeliano.intuitioncardbot.telegram.repository.BotUserRepository;
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

    private final BotUserRepository botUserRepository;

    public void handle(Message updateMessage) {
        long chatId = updateMessage.getChatId();

        BotMessage message = BotMessage.builder()
                .chatId(chatId)
                .telegramClient(telegramClient)
                .text(BotResponse.Error.UNKNOWN_TEXT_MESSAGE)
                .keyboard(BotKeyboard.mainMenu())
                .build();

        long messageId = message.send();
        BotUser botUser = botUserRepository.findById(chatId).get();
    }
}
