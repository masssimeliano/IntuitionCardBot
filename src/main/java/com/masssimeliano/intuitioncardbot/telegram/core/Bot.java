package com.masssimeliano.intuitioncardbot.telegram.core;
import com.masssimeliano.intuitioncardbot.telegram.handler.callback.AnswerCallbackHandler;
import com.masssimeliano.intuitioncardbot.telegram.handler.command.ErrorCommandHandler;
import com.masssimeliano.intuitioncardbot.telegram.handler.command.StartCommandHandler;
import com.masssimeliano.intuitioncardbot.telegram.handler.command.UnknownCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final BotProperties properties;
    private final StartCommandHandler startCommandHandler;
    private final ErrorCommandHandler errorCommandHandler;
    private final UnknownCommandHandler unknownCommandHandler;
    private final AnswerCallbackHandler answerCallHandler;

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
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            answerCallHandler.handle(callbackQuery);

            String callbackText = callbackQuery.getData();

            if (callbackText == null || callbackText.isBlank()) {
                return;
            }

            String[] callbackTextParts = callbackText.split(":");
            String root = callbackTextParts[0];

            switch (root) {
                case "nav":

                    break;
                case "mode":
                    break;
                case "pick" -> handlePick(p, chatId, messageId);
                case "stats" -> handleStats(p, chatId, messageId);
                case "again" -> handleAgain(chatId, messageId);
                default -> log.warn("Unknown callback root: {}", data);
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.debug("Registered bot running state is: {}", botSession.isRunning());
    }
}
