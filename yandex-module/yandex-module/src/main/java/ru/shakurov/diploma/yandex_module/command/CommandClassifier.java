package ru.shakurov.diploma.yandex_module.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CommandClassifier {

    public static CommandType classify(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasDocument()) {
                return CommandType.DOCUMENT;
            }
            List<MessageEntity> entities = message.getEntities();

            Optional<MessageEntity> first = entities.stream()
                    .filter(messageEntity -> "bot_command".equals(messageEntity.getType()))
                    .findFirst();
            if (first.isPresent()) {
                MessageEntity messageEntity = first.get();
                String text = messageEntity.getText();
                if (text.equals("/start")) {
                    return CommandType.START;
                }
                if (text.equals("/list")) {
                    return CommandType.LIST;
                }
                if (text.equals("/all")) {
                    return CommandType.ALL;
                }
            }
        } else if (update.hasCallbackQuery()) {
            return CommandType.ALL;
        }
        return null;
    }
}
