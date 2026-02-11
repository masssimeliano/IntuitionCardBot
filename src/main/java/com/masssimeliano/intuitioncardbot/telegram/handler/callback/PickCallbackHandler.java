package com.masssimeliano.intuitioncardbot.telegram.handler.callback;

import com.masssimeliano.intuitioncardbot.game.model.GameCard;
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

import java.security.SecureRandom;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PickCallbackHandler implements CallbackHandler {

    private final TelegramClient telegramClient;

    private final BotUserRepository botUserRepository;
    private final GameHistoryRepository gameHistoryRepository;

    private static final SecureRandom RND = new SecureRandom();
    private static final List<GameCard> DECK = GameCard.getAllCards();

    public void handle(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        BotUser botUser = botUserRepository.findById(chatId).orElseThrow();
        int lastMessageId = botUser.getLastMessageId();

        BotMessage botMessage = BotMessage.builder()
                .chatId(chatId)
                .telegramClient(telegramClient)
                .build();


        if ("again".equalsIgnoreCase(data)) {
            String lastMode = upper(botUser.getLastMode()); // "COLOR|SUIT|RANK|FULL"
            showModePick(botMessage, lastMode);
            botMessage.edit(lastMessageId);
            return;
        }

        // -------- PARSE --------
        String[] parts = data.split(":");
        if (parts.length < 3 || !"pick".equals(parts[0])) {
            botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
            botMessage.setKeyboard(BotKeyboard.mainMenu());
            botMessage.edit(lastMessageId);
            return;
        }

        String mode = upper(parts[1]);   // COLOR | SUIT | RANK | FULL
        GameCard actual = randomCard();

        switch (mode) {

            // -------- COLOR --------
            case "COLOR" -> {
                // pick:COLOR:RED|BLACK
                String userColor = upper(parts[2]); // RED/BLACK
                String actualColor = colorOf(actual.getSuit()); // RED/BLACK
                boolean correct = userColor.equals(actualColor);

                botUser.setLastMode("COLOR");
                botUserRepository.save(botUser);

                saveHistory(chatId, GameType.COLOR, correct);

                botMessage.setText(resultTextColor(userColor, actual, correct));
                botMessage.setKeyboard(BotKeyboard.afterRound());
            }

            // -------- SUIT --------
            case "SUIT" -> {
                // pick:SUIT:SPADES|HEARTS|DIAMONDS|CLUBS
                String userSuit = upper(parts[2]);
                String actualSuit = actual.getSuit().name();
                boolean correct = userSuit.equals(actualSuit);

                botUser.setLastMode("SUIT");
                botUserRepository.save(botUser);

                saveHistory(chatId, GameType.SUIT, correct);

                botMessage.setText(resultTextSuit(userSuit, actual, correct));
                botMessage.setKeyboard(BotKeyboard.afterRound());
            }

            // -------- RANK --------
            case "RANK" -> {
                // pick:RANK:A|K|Q|J|10..2
                String userRankRaw = parts[2];
                GameCard.CardRank userRank = parseRank(userRankRaw);
                GameCard.CardRank actualRank = actual.getRank();
                boolean correct = userRank == actualRank;

                botUser.setLastMode("RANK");
                botUserRepository.save(botUser);

                saveHistory(chatId, GameType.RANK, correct);

                botMessage.setText(resultTextRank(userRank, actual, correct));
                botMessage.setKeyboard(BotKeyboard.afterRound());
            }

            // -------- FULL / ALL --------
            case "FULL" -> {
                // Шаг 1: pick:FULL:RANK:A|K|...
                if ("RANK".equals(upper(parts[2]))) {
                    if (parts.length < 4) {
                        botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                        botMessage.setKeyboard(BotKeyboard.modesMenu());
                    } else {
                        GameCard.CardRank pickedRank = parseRank(parts[3]);

                        botUser.setLastMode("FULL");
                        botUser.setPendingFullRank(pickedRank.name()); // храним enum name
                        botUserRepository.save(botUser);

                        botMessage.setText(BotResponse.Pick.FULL_AFTER_RANK); // "Тепер обери масть"
                        botMessage.setKeyboard(BotKeyboard.fullPick());
                    }

                    botMessage.edit(lastMessageId);
                    return; // ВАЖНО: это промежуточный шаг, раунд ещё не завершён
                }

                // Шаг 2: pick:FULL:SUIT:SPADES|...
                if ("SUIT".equals(upper(parts[2]))) {
                    if (parts.length < 4) {
                        botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                        botMessage.setKeyboard(BotKeyboard.modesMenu());
                        break;
                    }

                    String pendingRankName = upper(botUser.getPendingFullRank());
                    if (pendingRankName == null) {
                        botMessage.setText(BotResponse.Navigation.FULL_RANK_CHOICE);
                        botMessage.setKeyboard(BotKeyboard.rankPick("pick:FULL:RANK:"));
                        break;
                    }

                    GameCard.CardRank userRank = GameCard.CardRank.valueOf(pendingRankName);
                    GameCard.CardSuit userSuit = GameCard.CardSuit.valueOf(upper(parts[3]));

                    boolean correct = (userRank == actual.getRank()) && (userSuit == actual.getSuit());

                    botUser.setLastMode("FULL");
                    botUser.setPendingFullRank(null);
                    botUserRepository.save(botUser);

                    saveHistory(chatId, GameType.ALL, correct);

                    botMessage.setText(resultTextFull(userRank, userSuit, actual, correct));
                    botMessage.setKeyboard(BotKeyboard.afterRound());
                    break;
                }

                botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                botMessage.setKeyboard(BotKeyboard.modesMenu());
            }

            default -> {
                botMessage.setText(BotResponse.Navigation.UNKNOWN_ERROR);
                botMessage.setKeyboard(BotKeyboard.modesMenu());
            }
        }

        botMessage.edit(lastMessageId);
    }

    // ------------------ persistence ------------------

    private void saveHistory(long chatId, GameType type, boolean correct) {
        gameHistoryRepository.save(
                GameHistory.builder()
                        .chatId(chatId)
                        .gameType(type)
                        .result(correct)
                        .build()
        );
    }

    // ------------------ random + parsing ------------------

    private static GameCard randomCard() {
        return DECK.get(RND.nextInt(DECK.size()));
    }

    private static String colorOf(GameCard.CardSuit suit) {
        return (suit == GameCard.CardSuit.HEARTS || suit == GameCard.CardSuit.DIAMONDS) ? "RED" : "BLACK";
    }

    private static GameCard.CardRank parseRank(String raw) {
        String r = upper(raw);
        return switch (r) {
            case "A" -> GameCard.CardRank.ACE;
            case "K" -> GameCard.CardRank.KING;
            case "Q" -> GameCard.CardRank.QUEEN;
            case "J" -> GameCard.CardRank.JACK;
            case "10" -> GameCard.CardRank.ONE;   // ⚠️ если "ONE" у тебя = 10, ок; если нет — скажи, поправим
            case "9" -> GameCard.CardRank.NINE;
            case "8" -> GameCard.CardRank.EIGHT;
            case "7" -> GameCard.CardRank.SEVEN;
            case "6" -> GameCard.CardRank.SIX;
            case "5" -> GameCard.CardRank.FIVE;
            case "4" -> GameCard.CardRank.FOUR;
            case "3" -> GameCard.CardRank.THREE;
            case "2" -> GameCard.CardRank.TWO;
            default -> throw new IllegalArgumentException("Bad rank: " + raw);
        };
    }

    private static String upper(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t.toUpperCase(Locale.ROOT);
    }

    // ------------------ UI (text) ------------------

    private static String prettyColor(String color) {
        return "RED".equals(color) ? "🔴 Червоний" : "⚫ Чорний";
    }

    private static String suitEmoji(GameCard.CardSuit suit) {
        return switch (suit) {
            case SPADES -> "♠️";
            case HEARTS -> "♥️";
            case DIAMONDS -> "♦️";
            case CLUBS -> "♣️";
        };
    }

    private static String prettySuit(GameCard.CardSuit suit) {
        return switch (suit) {
            case SPADES -> "♠️ Піки";
            case HEARTS -> "♥️ Черви";
            case DIAMONDS -> "♦️ Бубни";
            case CLUBS -> "♣️ Трефи";
        };
    }

    private static String prettyRank(GameCard.CardRank rank) {
        return switch (rank) {
            case ACE -> "A";
            case KING -> "K";
            case QUEEN -> "Q";
            case JACK -> "J";
            case NINE -> "9";
            case EIGHT -> "8";
            case SEVEN -> "7";
            case SIX -> "6";
            case FIVE -> "5";
            case FOUR -> "4";
            case THREE -> "3";
            case TWO -> "2";
            case ONE -> "10"; // ⚠️ см. комментарий в parseRank
        };
    }

    private static String cardPretty(GameCard card) {
        return prettyRank(card.getRank()) + suitEmoji(card.getSuit());
    }

    private static String verdict(boolean ok) {
        return ok ? "✅ *Вірно!*" : "❌ *Невірно.*";
    }

    private static String resultTextColor(String userColor, GameCard actual, boolean ok) {
        String actualColor = colorOf(actual.getSuit());
        return ""
                + "🎴 *Режим: Колір*\n\n"
                + "Твій вибір: " + prettyColor(userColor) + "\n"
                + "Карта була: " + cardPretty(actual) + " (" + prettyColor(actualColor) + ")\n\n"
                + verdict(ok);
    }

    private static String resultTextSuit(String userSuit, GameCard actual, boolean ok) {
        GameCard.CardSuit us = GameCard.CardSuit.valueOf(userSuit);
        return ""
                + "🎴 *Режим: Масть*\n\n"
                + "Твій вибір: " + prettySuit(us) + "\n"
                + "Карта була: " + cardPretty(actual) + " (" + prettySuit(actual.getSuit()) + ")\n\n"
                + verdict(ok);
    }

    private static String resultTextRank(GameCard.CardRank userRank, GameCard actual, boolean ok) {
        return ""
                + "🎴 *Режим: Номінал*\n\n"
                + "Твій вибір: " + prettyRank(userRank) + "\n"
                + "Карта була: " + cardPretty(actual) + " (номінал " + prettyRank(actual.getRank()) + ")\n\n"
                + verdict(ok);
    }

    private static String resultTextFull(GameCard.CardRank userRank, GameCard.CardSuit userSuit, GameCard actual, boolean ok) {
        String userCard = prettyRank(userRank) + suitEmoji(userSuit);
        return ""
                + "🃏 *Режим: Повна карта*\n\n"
                + "Твій вибір: " + userCard + "\n"
                + "Карта була: " + cardPretty(actual) + "\n\n"
                + verdict(ok);
    }

    // ------------------ show mode picks for "again" ------------------

    private static void showModePick(BotMessage botMessage, String lastMode) {
        if (lastMode == null) {
            botMessage.setText(BotResponse.Navigation.MODE_CHOICE);
            botMessage.setKeyboard(BotKeyboard.modesMenu());
            return;
        }

        switch (lastMode) {
            case "COLOR" -> {
                botMessage.setText(BotResponse.Mode.COLOR_CHOICE);
                botMessage.setKeyboard(BotKeyboard.colorPick());
            }
            case "SUIT" -> {
                botMessage.setText(BotResponse.Mode.SUIT_CHOICE);
                botMessage.setKeyboard(BotKeyboard.suitPick());
            }
            case "RANK" -> {
                botMessage.setText(BotResponse.Mode.RANK_CHOICE);
                botMessage.setKeyboard(BotKeyboard.rankPick("pick:RANK:"));
            }
            case "FULL" -> {
                botMessage.setText(BotResponse.Navigation.FULL_RANK_CHOICE);
                botMessage.setKeyboard(BotKeyboard.rankPick("pick:FULL:RANK:"));
            }
            default -> {
                botMessage.setText(BotResponse.Navigation.MODE_CHOICE);
                botMessage.setKeyboard(BotKeyboard.modesMenu());
            }
        }
    }
}
