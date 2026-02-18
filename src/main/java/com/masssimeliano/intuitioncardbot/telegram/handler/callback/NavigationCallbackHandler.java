package com.masssimeliano.intuitioncardbot.telegram.handler.callback;

import com.masssimeliano.intuitioncardbot.game.model.GameType;
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
public class NavigationCallbackHandler implements CallbackHandler {

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
            case "main" -> {
                botMessage.setText(BotResponse.Navigation.ACTION_CHOICE);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
            }
            case "modes" -> {
                botMessage.setText(BotResponse.Navigation.MODE_CHOICE);
                botMessage.setKeyboard(BotKeyboard.modesMenu());
            }
            case "stats" -> {
                botMessage.setText(BotResponse.Navigation.STATISTICS_CHOICE);
                botMessage.setKeyboard(BotKeyboard.statsMenu());
            }
            case "again" -> {
                BotUser botUser = botUserRepository.findById(chatId).get();
                GameType gameType = botUser.getLastGameType();

                switch (gameType) {
                    case GameType.COLOR -> {
                        botMessage.setText(BotResponse.Mode.COLOR_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.colorPick());
                    }
                    case GameType.SUIT -> {
                        botMessage.setText(BotResponse.Mode.SUIT_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.suitPick("pick:SUIT:"));
                    }
                    case GameType.RANK -> {
                        botMessage.setText(BotResponse.Mode.RANK_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.rankPick("pick:RANK:"));
                    }
                    case GameType.FULL -> {
                        botMessage.setText(BotResponse.Mode.FULL_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.rankPick("pick:FULL_RANK:"));
                    }
                    default -> {
                        botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                        botMessage.setKeyboard(BotKeyboard.mainMenu());
                        log.warn("Unknown game type by trying to play again: {}", root);
                    }
                }
            }
            case "about" -> {
                botMessage.setText(BotResponse.Navigation.ABOUT);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
            }
            default -> {
                botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                log.warn("Unknown callback data nav part: {}", root);
            }
        }

        BotUser botUser = botUserRepository.findById(chatId).get();
        int lastMessageId = botUser.getLastMessageId();

        botMessage.edit(lastMessageId);
    }
}
