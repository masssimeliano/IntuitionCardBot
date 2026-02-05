package com.masssimeliano.intuitioncardbot.telegram.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot")
@Getter
@RequiredArgsConstructor
public class BotProperties {
    private final String username;
    private final String token;
}
