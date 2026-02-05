package com.masssimeliano.intuitioncardbot.game.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameCard {

    private CardRank rank;

    private CardSuit suit;

    public enum CardRank {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        JACK,
        QUEEN,
        KING,
        ACE;
    }

    public enum CardSuit {
        HEARTS,
        DIAMONDS,
        CLUBS,
        SPADES;
    }

    public static List<GameCard> getAllCards() {
        List<GameCard> cards = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank rank : CardRank.values()) {
                cards.add(new GameCard(rank, suit));
            }
        }
        return cards;
    }

    public static List<GameCard> getAllCardsOfSuit(CardSuit suit) {
        List<GameCard> cards = new ArrayList<>();
        for (CardRank rank : CardRank.values()) {
            cards.add(new GameCard(rank, suit));
        }
        return cards;
    }

    public static List<GameCard> getAllCardsOfRank(CardRank rank) {
        List<GameCard> cards = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            cards.add(new GameCard(rank, suit));
        }
        return cards;
    }
}
