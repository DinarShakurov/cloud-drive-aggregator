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
import ru.shakurov.diploma.yandex_module.service.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class StartCommandHandler implements CommandHandler {

    private final YandexHelper yandexHelper;
    private final GoogleHelper googleHelper;
    private final DropboxHelper dropboxHelper;
    private final UserService userService;

    @Override
    public CommandType getCommandType() {
        return CommandType.START;
    }

    @Override
    public List<BotApiMethod<?>> handleCommand(Update update) {
        String clientId = update.getMessage().getFrom().getId().toString();
        userService.findOrCreate(clientId);

        String yandexOAuthLink = yandexHelper.generateOAuthLink(clientId);
        String googleOAuthLink = googleHelper.generateOAuthLink(clientId);
        String dropboxOAuthLink = dropboxHelper.generateOAuthLink(clientId);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .parseMode(ParseMode.MARKDOWNV2)
                .text("""
                        Подключение облачных дисков:
                        """)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(
                                InlineKeyboardButton.builder()
                                        .text("Яндекс.Диск")
                                        .url(yandexOAuthLink)
                                        .build()
                        ))
                        .keyboardRow(List.of(
                                InlineKeyboardButton.builder()
                                        .text("Google Диск")
                                        .url(googleOAuthLink)
                                        .build()
                        ))
                        .keyboardRow(List.of(
                                InlineKeyboardButton.builder()
                                        .text("Dropbox")
                                        .url(dropboxOAuthLink)
                                        .build()
                        ))
                        .build())
                .build();
        return List.of(sendMessage);
    }
}
