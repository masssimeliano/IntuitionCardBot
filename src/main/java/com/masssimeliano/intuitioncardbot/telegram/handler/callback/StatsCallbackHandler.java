package com.masssimeliano.intuitioncardbot.telegram.handler.callback;

import com.masssimeliano.intuitioncardbot.game.repository.GameHistoryRepository;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsCallbackHandler implements CallbackHandler {

    private static final ZoneId ZONE = ZoneId.of("Europe/Berlin");

    private final TelegramClient telegramClient;

    private final BotUserRepository botUserRepository;
    private final GameHistoryRepository gameHistoryRepository;

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
            case "TOTAL" -> {
                int countAll = gameHistoryRepository.countByChatId(chatId);
                int countCorrect = gameHistoryRepository.countByChatIdAndResultTrue(chatId);
                botMessage.setText(BotResponse.Stats.RESULT_FULL("📈 За весь час", countAll, countCorrect));
                botMessage.setKeyboard(BotKeyboard.statsMenu());
            }
            case "TODAY" -> {
                Instant from = startOfToday();
                Instant to = startOfTomorrow();
                int countAll = gameHistoryRepository.countByChatIdAndCreatedAtBetween(chatId, from, to);
                int countCorrect = gameHistoryRepository.countByChatIdAndResultTrueAndCreatedAtBetween(chatId, from, to);
                botMessage.setText(BotResponse.Stats.RESULT_FULL("📅 Сьогодні", countAll, countCorrect));
                botMessage.setKeyboard(BotKeyboard.statsMenu());
            }
            case "YESTERDAY" -> {
                Instant from = startOfYesterday();
                Instant to = startOfToday();
                int countAll = gameHistoryRepository.countByChatIdAndCreatedAtBetween(chatId, from, to);
                int countCorrect = gameHistoryRepository.countByChatIdAndResultTrueAndCreatedAtBetween(chatId, from, to);
                botMessage.setText(BotResponse.Stats.RESULT_FULL("🕐 Вчора", countAll, countCorrect));
                botMessage.setKeyboard(BotKeyboard.statsMenu());
            }
            case "WEEK" -> {
                Instant to = startOfTomorrow();
                Instant from = startOfDay(LocalDate.now(ZONE).minusDays(6));
                int countAll = gameHistoryRepository.countByChatIdAndCreatedAtBetween(chatId, from, to);
                int countCorrect = gameHistoryRepository.countByChatIdAndResultTrueAndCreatedAtBetween(chatId, from, to);
                botMessage.setText(BotResponse.Stats.RESULT_FULL("⏳ Тиждень", countAll, countCorrect));
                botMessage.setKeyboard(BotKeyboard.statsMenu());
            }
            case "MONTH" -> {
                Instant to = startOfTomorrow();
                Instant from = startOfDay(LocalDate.now(ZONE).minusDays(29));
                int countAll = gameHistoryRepository.countByChatIdAndCreatedAtBetween(chatId, from, to);
                int countCorrect = gameHistoryRepository.countByChatIdAndResultTrueAndCreatedAtBetween(chatId, from, to);
                botMessage.setText(BotResponse.Stats.RESULT_FULL("⏰ Місяць", countAll, countCorrect));
                botMessage.setKeyboard(BotKeyboard.statsMenu());
            }
            default -> {
                botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                log.warn("Unknown callback data stats part: {}", root);
            }
        }

        BotUser botUser = botUserRepository.findById(chatId).orElseThrow();
        int lastMessageId = botUser.getLastMessageId();

        botMessage.edit(lastMessageId);
    }

    private Instant startOfToday() {
        return startOfDay(LocalDate.now(ZONE));
    }

    private Instant startOfTomorrow() {
        return startOfDay(LocalDate.now(ZONE).plusDays(1));
    }

    private Instant startOfYesterday() {
        return startOfDay(LocalDate.now(ZONE).minusDays(1));
    }

    private Instant startOfDay(LocalDate date) {
        return date.atStartOfDay(ZONE).toInstant();
    }
}
