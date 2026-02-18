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
            case "main":
                botMessage.setText(BotResponse.Navigation.ACTION_CHOICE);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;
            case "modes":
                botMessage.setText(BotResponse.Navigation.MODE_CHOICE);
                botMessage.setKeyboard(BotKeyboard.modesMenu());
                break;
            case "stats":
                botMessage.setText(BotResponse.Navigation.STATISTICS_CHOICE);
                botMessage.setKeyboard(BotKeyboard.statsMenu());
                break;
            case "again":
                BotUser botUser = botUserRepository.findById(chatId).get();
                GameType gameType = botUser.getLastGameType();

                switch (gameType) {
                    case GameType.COLOR:
                        botMessage.setText(BotResponse.Mode.COLOR_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.colorPick());
                        break;
                    case GameType.SUIT:
                        botMessage.setText(BotResponse.Mode.SUIT_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.suitPick("pick:SUIT:"));
                        break;
                    case GameType.RANK:
                        botMessage.setText(BotResponse.Mode.RANK_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.rankPick("pick:RANK:"));
                        break;
                    case GameType.ALL:
                        botMessage.setText(BotResponse.Mode.FULL_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.rankPick("pick:FULL_RANK:"));
                        break;
                    default:
                        botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                        botMessage.setKeyboard(BotKeyboard.mainMenu());
                        break;
                }

                break;
            case "about":
                botMessage.setText(BotResponse.Navigation.ABOUT);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;
            default:
                botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;
        }

        BotUser botUser = botUserRepository.findById(chatId).get();
        int lastMessageId = botUser.getLastMessageId();

        botMessage.edit(lastMessageId);
    }
}
