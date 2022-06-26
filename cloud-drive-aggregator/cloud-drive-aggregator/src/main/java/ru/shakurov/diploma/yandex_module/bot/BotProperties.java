package ru.shakurov.diploma.yandex_module.bot;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@ConfigurationProperties("bot")
@Getter
@Setter
@ToString
public class BotProperties extends DefaultBotOptions {
    private String webhookUrl;
    private String token;
    private String username;
    private String path;
}
