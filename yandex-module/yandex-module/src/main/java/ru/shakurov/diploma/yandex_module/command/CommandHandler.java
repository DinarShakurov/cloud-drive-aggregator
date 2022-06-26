package ru.shakurov.diploma.yandex_module.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface CommandHandler {

    CommandType getCommandType();


    List<BotApiMethod<?>> handleCommand(Update update);

}
