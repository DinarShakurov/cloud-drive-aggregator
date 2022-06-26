package ru.shakurov.diploma.yandex_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.shakurov.diploma.yandex_module.bot.BotProperties;

@SpringBootApplication
@EnableConfigurationProperties(BotProperties.class)
public class YandexModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(YandexModuleApplication.class, args);
    }
}
