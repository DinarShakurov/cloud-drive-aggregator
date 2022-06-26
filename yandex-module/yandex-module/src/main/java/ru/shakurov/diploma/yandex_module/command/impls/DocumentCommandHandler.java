package ru.shakurov.diploma.yandex_module.command.impls;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.shakurov.diploma.yandex_module.bot.Bot;
import ru.shakurov.diploma.yandex_module.client.AggregatorClient;
import ru.shakurov.diploma.yandex_module.client.DropboxApiClient;
import ru.shakurov.diploma.yandex_module.command.CommandHandler;
import ru.shakurov.diploma.yandex_module.command.CommandType;
import ru.shakurov.diploma.yandex_module.dto.FileUploadedEvent;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;
import ru.shakurov.diploma.yandex_module.entity.User;
import ru.shakurov.diploma.yandex_module.service.TokenService;
import ru.shakurov.diploma.yandex_module.service.UserService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentCommandHandler implements CommandHandler {

    @Lazy
    @Autowired
    private Bot bot;
    private final UserService userService;
    private final AggregatorClient aggregatorClient;

    @Override
    public CommandType getCommandType() {
        return CommandType.DOCUMENT;
    }

    @SneakyThrows
    @Override
    @Transactional(readOnly = true)
    public List<BotApiMethod<?>> handleCommand(Update update) {
        Message message = update.getMessage();
        User user = userService.findOrCreate(message.getFrom().getId().toString());
        Document document = message.getDocument();

        GetFile getFileRequest = GetFile.builder().fileId(document.getFileId()).build();
        //File file = bot.execute(getFileRequest);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            try {
                aggregatorClient.uploadFile(user.getTokens(), null,
                        new FileUploadedEvent(this, user.getUserId(), message.getMessageId().toString()));
            } catch (Exception e) {
                log.error("Error: ", e);
            }
        });
        Thread.sleep(1000L);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getUserId())
                .text("Как только файл будет загружен на диск - придет сообщение.")
                .build();
        return List.of(sendMessage);
    }
}
