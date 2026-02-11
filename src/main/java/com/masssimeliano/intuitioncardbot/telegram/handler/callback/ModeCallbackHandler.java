package com.masssimeliano.intuitioncardbot.telegram.handler.callback;

import com.masssimeliano.intuitioncardbot.telegram.core.BotKeyboard;
import com.masssimeliano.intuitioncardbot.telegram.core.BotMessage;
import com.masssimeliano.intuitioncardbot.telegram.core.BotResponse;
import com.masssimeliano.intuitioncardbot.telegram.model.BotUser;
import com.masssimeliano.intuitioncardbot.telegram.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModeCallbackHandler implements CallbackHandler {

    private final TelegramClient telegramClient;

    private final BotUserRepository botUserRepository;

    public void handle(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();

        String[] callbackTextParts = callbackData.split(":");
        String root = callbackTextParts[1];

        long chatId = callbackQuery.getMessage().getChatId();

        BotMessage botMessage = BotMessage.builder()
                .chatId(chatId)
                .telegramClient(telegramClient)
                .build();

        switch (root) {
            case "COLOR":
                botMessage.setText(BotResponse.Mode.COLOR_CHOICE);
                botMessage.setKeyboard(BotKeyboard.colorPick());
                break;

            case "SUIT":
                botMessage.setText(BotResponse.Mode.SUIT_CHOICE);
                botMessage.setKeyboard(BotKeyboard.suitPick());
                break;

            case "RANK":
                botMessage.setText(BotResponse.Mode.RANK_CHOICE);
                botMessage.setKeyboard(BotKeyboard.rankPick("pick:RANK:"));
                break;

            case "FULL":
                botMessage.setText(BotResponse.Mode.FULL_CHOICE);
                botMessage.setKeyboard(BotKeyboard.rankPick("pick:FULL:RANK:"));
                break;

            default:
                botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;
        }

        BotUser botUser = botUserRepository.findById(chatId).orElseThrow();
        int lastMessageId = botUser.getLastMessageId();

        botMessage.edit(lastMessageId);
    }
}
