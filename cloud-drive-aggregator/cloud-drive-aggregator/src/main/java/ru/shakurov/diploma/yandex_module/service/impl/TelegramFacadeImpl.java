package ru.shakurov.diploma.yandex_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.shakurov.diploma.yandex_module.command.CommandHandler;
import ru.shakurov.diploma.yandex_module.command.CommandType;
import ru.shakurov.diploma.yandex_module.service.TelegramFacade;

import java.util.List;
import java.util.Map;

import static ru.shakurov.diploma.yandex_module.command.CommandClassifier.classify;

@Service
@RequiredArgsConstructor
@Slf4j
class TelegramFacadeImpl implements TelegramFacade {

    private final Map<CommandType, CommandHandler> handlers;

    @Override
    public List<BotApiMethod<?>> handleUpdate(Update update) {
        log.info("Update: {}", update);
        CommandType commandType = classify(update);
        if (commandType != null) {
            CommandHandler commandHandler = handlers.get(commandType);
            return commandHandler.handleCommand(update);
        }
        return null;
    }


}
