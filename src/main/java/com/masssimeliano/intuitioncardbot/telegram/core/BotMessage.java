package com.masssimeliano.intuitioncardbot.telegram.core;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class BotMessage {

    private final long chatId;
    private InlineKeyboardMarkup keyboard;
    private String text;

    @NonNull
    private final TelegramClient telegramClient;

    public long send() {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();

        long messageId = 0L;
        try {
            Message sentMessage = telegramClient.execute(message);
            messageId = sentMessage.getMessageId();
        } catch (TelegramApiException e) {
            log.error("Error sending the message: {}", e.getMessage());
        }

        return messageId;
    }
}
