package ru.shakurov.diploma.yandex_module.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.facilities.filedownloader.TelegramFileDownloader;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.shakurov.diploma.yandex_module.bot.Bot;

@RestController
@RequestMapping("/callback/bot${bot.token}")
@RequiredArgsConstructor
@Slf4j
public class BotController {

    private final Bot bot;

    @PostMapping
    public void botApiMethod(@RequestBody Update update) {
        bot.onWebhookUpdateReceived(update);
    }
}
