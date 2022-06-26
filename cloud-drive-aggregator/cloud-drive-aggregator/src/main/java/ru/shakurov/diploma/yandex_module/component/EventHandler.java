package ru.shakurov.diploma.yandex_module.component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.shakurov.diploma.yandex_module.bot.Bot;
import ru.shakurov.diploma.yandex_module.dto.FileUploadedEvent;
import ru.shakurov.diploma.yandex_module.dto.NewAuthEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventHandler {

    private final Bot bot;

    @EventListener(NewAuthEvent.class)
    public void handleNewAuthEvent(NewAuthEvent event) {
        try {
            SendMessage message = SendMessage.builder()
                    .chatId(event.getClientId())
                    .text(String.format("[%s] Авторизация прошла успешно.", event.getDiskType()))
                    .build();
            bot.send(message);
        } catch (TelegramApiException e) {
            log.error("Error", e);
        }
    }

    @SneakyThrows
    @EventListener(FileUploadedEvent.class)
    public void handleFileUploadedEvent(FileUploadedEvent event) {
        SendMessage message = SendMessage.builder()
                .chatId(event.getClientId())
                .replyToMessageId(Integer.valueOf(event.getReplyToId()))
                .text("Файл успешно загружен.")
                .build();
        bot.send(message);
    }
}
