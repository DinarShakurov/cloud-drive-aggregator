package ru.shakurov.diploma.yandex_module.command.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.shakurov.diploma.yandex_module.command.CommandHandler;
import ru.shakurov.diploma.yandex_module.command.CommandType;
import ru.shakurov.diploma.yandex_module.helper.DropboxHelper;
import ru.shakurov.diploma.yandex_module.helper.GoogleHelper;
import ru.shakurov.diploma.yandex_module.helper.YandexHelper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConnectCommandHandler implements CommandHandler {

    private final StartCommandHandler startCommandHandler;

    @Override
    public CommandType getCommandType() {
        return CommandType.CONNECT;
    }

    @Override
    public List<BotApiMethod<?>> handleCommand(Update update) {
        return startCommandHandler.handleCommand(update);
    }
}
