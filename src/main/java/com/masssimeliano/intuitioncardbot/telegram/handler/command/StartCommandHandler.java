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
@RequiredArgsConstructor
@Component
public class StartCommandHandler implements CommandHandler {

    private final TelegramClient telegramClient;

    private final BotUserRepository botUserRepository;

    public void handle(Message updateMessage) {
        long chatId = updateMessage.getChatId();
        String firstName = updateMessage.getFrom().getFirstName();

        String text = BotResponse.Command.GREETING(firstName) + "\n";

        boolean alreadyRegistered = botUserRepository.existsById(chatId);
        if (alreadyRegistered) {
            text = text + BotResponse.Command.ALREADY_REGISTERED;
        } else {
            text = text + BotResponse.Command.NOT_REGISTERED;
        }

        BotMessage message = BotMessage.builder()
                .chatId(chatId)
                .telegramClient(telegramClient)
                .text(text)
                .keyboard(BotKeyboard.mainMenu())
                .build();

        BotUser botUser;
        long messageId = message.send();
        if (!alreadyRegistered) {
            botUser = BotUser.builder()
                    .chatId(chatId)
                    .firstName(firstName)
                    .username(updateMessage.getFrom().getUserName())
                    .lastMessageId(messageId)
                    .build();

            botUserRepository.save(botUser);
        } else {
            botUser = botUserRepository.findById(chatId).get();
            botUser.setLastMessageId(messageId);
        }

        botUserRepository.save(botUser);
    }
}
