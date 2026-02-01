package com.masssimeliano.intuitioncardbot.telegram.repository;

import com.masssimeliano.intuitioncardbot.telegram.model.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotUserRepository extends JpaRepository<BotUser, Long> {
}
