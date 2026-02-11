package com.masssimeliano.intuitioncardbot.telegram.core;
import com.masssimeliano.intuitioncardbot.telegram.handler.callback.AnswerCallbackHandler;
import com.masssimeliano.intuitioncardbot.telegram.handler.callback.ModeCallbackHandler;
import com.masssimeliano.intuitioncardbot.telegram.handler.callback.NavigationCallbackHandler;
import com.masssimeliano.intuitioncardbot.telegram.handler.callback.PickCallbackHandler;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final BotProperties properties;
    private final StartCommandHandler startCommandHandler;
    private final UnknownCommandHandler unknownCommandHandler;
    private final AnswerCallbackHandler answerCallHandler;
    private final NavigationCallbackHandler navigationCallHandler;
    private final ModeCallbackHandler modeCallHandler;
    private final PickCallbackHandler pickCallHandler;

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
                        unknownCommandHandler.handle(updateMessage);
                }
            } else {
                unknownCommandHandler.handle(updateMessage);
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
                    navigationCallHandler.handle(callbackQuery);
                    break;
                case "mode":
                    modeCallHandler.handle(callbackQuery);
                    break;
                case "pick":
                    pickCallHandler.handle(callbackQuery);
                    break;
                default:
                    break;
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.debug("Registered bot running state is: {}", botSession.isRunning());
    }
}
