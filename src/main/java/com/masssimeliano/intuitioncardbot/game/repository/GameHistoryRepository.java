package com.masssimeliano.intuitioncardbot.game.repository;

import com.masssimeliano.intuitioncardbot.game.model.GameHistory;
import com.masssimeliano.intuitioncardbot.game.model.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {

    long countByChatIdAndGameType(Long chatId, GameType gameType);

    long countByChatIdAndGameTypeAndResultTrue(Long chatId, GameType gameType);
}
