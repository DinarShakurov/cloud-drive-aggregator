package ru.shakurov.diploma.yandex_module.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface TelegramFacade {
    List<BotApiMethod<?>> handleUpdate(Update update);
}
