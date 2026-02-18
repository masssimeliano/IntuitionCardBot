package com.masssimeliano.intuitioncardbot.game.model;

import com.masssimeliano.intuitioncardbot.telegram.model.BotUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "game_history")
public class GameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false, length = 50)
    private GameType gameType;

    @Column(name = "result", nullable = false)
    private Boolean result;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", insertable = false, updatable = false)
    private BotUser user;
}
