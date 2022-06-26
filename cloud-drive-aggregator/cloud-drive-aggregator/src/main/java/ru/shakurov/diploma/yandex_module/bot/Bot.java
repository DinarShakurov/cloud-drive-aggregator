package ru.shakurov.diploma.yandex_module.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.shakurov.diploma.yandex_module.service.TelegramFacade;

import java.util.List;

@Component
@Slf4j
public class Bot extends SpringWebhookBot {

    private final BotProperties options;
    private final TelegramFacade telegramFacade;

    public Bot(TelegramFacade telegramFacade,
               BotProperties options,
               SetWebhook setWebhook) throws TelegramApiException {
        super(options, setWebhook);
        this.options = options;
        this.telegramFacade = telegramFacade;
        this.setWebhook(setWebhook);
    }

    @Override
    public String getBotUsername() {
        return options.getUsername();
    }

    @Override
    public String getBotToken() {
        return options.getToken();
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        List<BotApiMethod<?>> list = telegramFacade.handleUpdate(update);
        if (list !=null) {
            for (BotApiMethod<?> botApiMethod : list) {
                sendApiMethod(botApiMethod);
            }
        }

        return null;
    }

    public void send(BotApiMethod<?> botApiMethod) throws TelegramApiException {
        sendApiMethod(botApiMethod);
    }

    @Override
    public String getBotPath() {
        return options.getPath();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
