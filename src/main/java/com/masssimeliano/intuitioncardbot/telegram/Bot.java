package com.masssimeliano.intuitioncardbot.telegram;
import com.masssimeliano.intuitioncardbot.telegram.handler.ErrorCommandHandler;
import com.masssimeliano.intuitioncardbot.telegram.handler.StartCommandHandler;
import com.masssimeliano.intuitioncardbot.telegram.handler.UnknownCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final BotProperties properties;
    private StartCommandHandler startCommandHandler;
    private ErrorCommandHandler errorCommandHandler;
    private UnknownCommandHandler unknownCommandHandler;

    private final TelegramClient telegramClient;

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            Message updateMessage = update.getMessage();

            if (updateMessage.hasText()) {
                String updateText = updateMessage.getText();

                switch (updateText) {
                    case "/start":
                        startCommandHandler.handle(updateMessage);
                        break;
                    default:
                        unknownCommandHandler.handle(updateMessage);                }
            } else {
                errorCommandHandler.handle(updateMessage);
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.debug("Registered bot running state is: {}", botSession.isRunning());
    }
}
