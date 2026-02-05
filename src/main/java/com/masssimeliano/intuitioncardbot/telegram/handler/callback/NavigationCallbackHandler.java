package com.masssimeliano.intuitioncardbot.telegram.handler.callback;

import com.masssimeliano.intuitioncardbot.telegram.core.BotKeyboard;
import com.masssimeliano.intuitioncardbot.telegram.core.BotMessage;
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

        System.out.println(root);

        switch (root) {
            case "main":
                botMessage.setText("👋 Привіт! Обери дію нижче:");
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;

            case "modes":
                botMessage.setText("🎴 Обери режим гри. Чим складніше — тим цікавіше 😉");
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;

            case "stats":
                botMessage.setText("📊 Обери період статистики:");
                botMessage.setKeyboard(BotKeyboard.statsMenu());
                break;

            case "about":
                botMessage.setText("ℹ️ Про бота...");
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;

            case "full_rank":
                botMessage.setText("🃏 Повна карта: спочатку обери *номінал* (A, K, Q, J, 10...2)");
                botMessage.setKeyboard(BotKeyboard.rankPick("pick:FULL:RANK:"));

            default:
                botMessage.setText("ℹ️ НЕПОН");
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;
        }

        BotUser botUser = botUserRepository.findById(chatId).get();
        int lastMessageId = botUser.getLastMessageId();

        botMessage.edit(lastMessageId);
    }
}
