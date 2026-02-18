package com.masssimeliano.intuitioncardbot.game.repository;

import com.masssimeliano.intuitioncardbot.game.model.GameHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {

    int countByChatId(Long chatId);

    int countByChatIdAndResultTrue(Long chatId);

    int countByChatIdAndCreatedAtBetween(Long chatId, Instant from, Instant to);

    int countByChatIdAndResultTrueAndCreatedAtBetween(Long chatId, Instant from, Instant to);
}
