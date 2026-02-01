package com.masssimeliano.intuitioncardbot.telegram.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Integer chatId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "username", length = 100)
    private String username;
}
