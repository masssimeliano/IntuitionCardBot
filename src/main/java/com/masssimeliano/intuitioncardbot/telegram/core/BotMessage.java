package com.masssimeliano.intuitioncardbot.telegram.core;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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

    public int send() {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();

        int messageId = 0;
        try {
            Message sentMessage = telegramClient.execute(message);
            messageId = sentMessage.getMessageId();
        } catch (TelegramApiException e) {
            log.error("Error sending the message: {}", e.getMessage());
        }

        return messageId;
    }

    public void delete(int messageId) {
        DeleteMessage message = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error deleting the message: {}", e.getMessage());
        }
    }

    public void edit(int messageId) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(keyboard)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error editing the message: {}", e.getMessage());
        }
    }
}
