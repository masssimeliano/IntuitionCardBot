package com.masssimeliano.intuitioncardbot.telegram.handler.callback;

import com.masssimeliano.intuitioncardbot.game.mapper.ColorMapper;
import com.masssimeliano.intuitioncardbot.game.mapper.SuitMapper;
import com.masssimeliano.intuitioncardbot.game.model.GameHistory;
import com.masssimeliano.intuitioncardbot.game.model.GameType;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class PickCallbackHandler implements CallbackHandler {

    private final TelegramClient telegramClient;

    private final BotUserRepository botUserRepository;
    private final GameHistoryRepository gameHistoryRepository;

    public void handle(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();

        String[] callbackTextParts = callbackData.split(":");
        String root = callbackTextParts[1];
        String gamePart = callbackTextParts[2];

        long chatId = callbackQuery.getMessage().getChatId();

        BotMessage botMessage = BotMessage.builder()
                .chatId(chatId)
                .telegramClient(telegramClient)
                .build();

        double p = Math.random();
        String actualSuit, actualRank, actualColor;
        boolean correctChoice;
        GameHistory gameHistory;
        BotUser botUser;

        switch (root) {
            case "COLOR":
                actualColor = "BLACK";
                if (Math.random() < 0.5) {
                    actualColor = "RED";
                }

                botMessage.setText(BotResponse.Pick.RESULT_COLOR(ColorMapper.mapToEmoji(gamePart), ColorMapper.mapToEmoji(actualColor)));
                botMessage.setKeyboard(BotKeyboard.afterRound());

                correctChoice = actualColor.equals(gamePart);

                gameHistory = GameHistory.builder()
                        .chatId(chatId)
                        .result(correctChoice)
                        .gameType(GameType.COLOR)
                        .createdAt(Instant.now())
                        .build();
                gameHistoryRepository.save(gameHistory);

                botUser = botUserRepository.findById(chatId).orElseThrow();
                botUser.setLastGameType(GameType.COLOR);
                botUserRepository.save(botUser);
                break;
            case "SUIT":
                actualSuit =
                        (p < 0.25)  ? "HEARTS" :
                                (p < 0.5)  ? "DIAMONDS" :
                                        (p < 0.75)  ? "CLUBS" : "SPADES";

                botMessage.setText(BotResponse.Pick.RESULT_SUIT(SuitMapper.mapToEmoji(gamePart), SuitMapper.mapToEmoji(actualSuit)));
                botMessage.setKeyboard(BotKeyboard.afterRound());

                correctChoice = actualSuit.equals(gamePart);

                gameHistory = GameHistory.builder()
                        .chatId(chatId)
                        .result(correctChoice)
                        .gameType(GameType.SUIT)
                        .createdAt(Instant.now())
                        .build();
                gameHistoryRepository.save(gameHistory);

                botUser = botUserRepository.findById(chatId).orElseThrow();
                botUser.setLastGameType(GameType.SUIT);
                botUserRepository.save(botUser);
                break;
            case "RANK":
                p = Math.random();
                actualRank =
                        (p < 1.0/13)  ? "A" :
                                (p < 2.0/13)  ? "K" :
                                        (p < 3.0/13)  ? "Q" :
                                                (p < 4.0/13)  ? "J" :
                                                        (p < 5.0/13)  ? "10" :
                                                                (p < 6.0/13)  ? "9" :
                                                                        (p < 7.0/13)  ? "8" :
                                                                                (p < 8.0/13)  ? "7" :
                                                                                        (p < 9.0/13)  ? "6" :
                                                                                                (p < 10.0/13) ? "5" :
                                                                                                        (p < 11.0/13) ? "4" :
                                                                                                                (p < 12.0/13) ? "3" : "2";

                botMessage.setText(BotResponse.Pick.RESULT_RANK(gamePart, actualRank));
                botMessage.setKeyboard(BotKeyboard.afterRound());

                correctChoice = actualRank.equals(gamePart);

                gameHistory = GameHistory.builder()
                        .chatId(chatId)
                        .result(correctChoice)
                        .gameType(GameType.RANK)
                        .createdAt(Instant.now())
                        .build();
                gameHistoryRepository.save(gameHistory);

                botUser = botUserRepository.findById(chatId).orElseThrow();
                botUser.setLastGameType(GameType.RANK);
                botUserRepository.save(botUser);
                break;
            case "FULL_RANK":
                botUser = botUserRepository.findById(chatId).orElseThrow();
                botUser.setLastRank(gamePart);
                botUserRepository.save(botUser);

                botMessage.setText(BotResponse.Pick.FULL_AFTER_RANK);
                botMessage.setKeyboard(BotKeyboard.suitPick("pick:FULL_SUIT:"));
                break;
            case "FULL_SUIT":
                p = Math.random();
                actualRank =
                        (p < 1.0/13)  ? "A" :
                                (p < 2.0/13)  ? "K" :
                                        (p < 3.0/13)  ? "Q" :
                                                (p < 4.0/13)  ? "J" :
                                                        (p < 5.0/13)  ? "10" :
                                                                (p < 6.0/13)  ? "9" :
                                                                        (p < 7.0/13)  ? "8" :
                                                                                (p < 8.0/13)  ? "7" :
                                                                                        (p < 9.0/13)  ? "6" :
                                                                                                (p < 10.0/13) ? "5" :
                                                                                                        (p < 11.0/13) ? "4" :
                                                                                                                (p < 12.0/13) ? "3" : "2";
                actualSuit =
                        (p < 0.25)  ? "HEARTS" :
                                (p < 0.5)  ? "DIAMONDS" :
                                        (p < 0.75)  ? "CLUBS" : "SPADES";

                botUser = botUserRepository.findById(chatId).orElseThrow();
                String lastRank = botUser.getLastRank();

                botMessage.setText(BotResponse.Pick.RESULT_FULL(
                        lastRank+ " " + SuitMapper.mapToEmoji(gamePart),
                        actualRank + " " + SuitMapper.mapToEmoji(actualSuit)));
                botMessage.setKeyboard(BotKeyboard.afterRound());

                correctChoice = actualRank.equals(lastRank) && actualSuit.equals(gamePart);

                gameHistory = GameHistory.builder()
                        .chatId(chatId)
                        .result(correctChoice)
                        .gameType(GameType.ALL)
                        .createdAt(Instant.now())
                        .build();
                gameHistoryRepository.save(gameHistory);

                botUser.setLastGameType(GameType.ALL);
                botUserRepository.save(botUser);
                break;
            default:
                botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                botMessage.setKeyboard(BotKeyboard.mainMenu());
                break;
        }

        botUser = botUserRepository.findById(chatId).orElseThrow();
        int lastMessageId = botUser.getLastMessageId();

        botMessage.edit(lastMessageId);
    }
}
