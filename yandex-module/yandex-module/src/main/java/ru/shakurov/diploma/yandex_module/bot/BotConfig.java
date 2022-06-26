package ru.shakurov.diploma.yandex_module.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.shakurov.diploma.yandex_module.command.CommandHandler;
import ru.shakurov.diploma.yandex_module.command.CommandType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class BotConfig {

    @Bean
    public SetWebhook setWebhook(BotProperties botProperties) {
        return SetWebhook.builder()
                .url(botProperties.getWebhookUrl())
                .build();
    }

    @Bean
    public Map<CommandType, CommandHandler> handlers(List<CommandHandler> commandHandlers) {
        return commandHandlers.stream()
                .collect(Collectors.toMap(CommandHandler::getCommandType, Function.identity()));
    }
}
