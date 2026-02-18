package com.masssimeliano.intuitioncardbot.telegram.model;

import com.masssimeliano.intuitioncardbot.game.model.GameType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "bot_users")
public class BotUser {

    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "last_rank", length = 2)
    private String lastRank;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_game_type", length = 50)
    private GameType lastGameType;

    @Column(name = "last_message_id")
    private Integer lastMessageId;
}
