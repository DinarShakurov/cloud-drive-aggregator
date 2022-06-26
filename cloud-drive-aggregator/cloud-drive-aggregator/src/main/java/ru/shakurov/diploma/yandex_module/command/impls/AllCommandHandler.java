package ru.shakurov.diploma.yandex_module.command.impls;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.shakurov.diploma.yandex_module.client.AggregatorClient;
import ru.shakurov.diploma.yandex_module.command.CommandHandler;
import ru.shakurov.diploma.yandex_module.command.CommandType;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;
import ru.shakurov.diploma.yandex_module.entity.User;
import ru.shakurov.diploma.yandex_module.service.UserService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWNV2;

@Component
@RequiredArgsConstructor
@Slf4j
public class AllCommandHandler implements CommandHandler {

    private final UserService userService;
    private final AggregatorClient aggregatorClient;

    @Override
    public CommandType getCommandType() {
        return CommandType.ALL;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BotApiMethod<?>> handleCommand(Update update) {
        User user = null;
        if (update.hasMessage()) {
            user = userService.findOrCreate(update.getMessage().getFrom().getId().toString());
            List<CloudFileDto> allFiles = aggregatorClient.getAllFiles(user.getTokens());
            String text = buildText(allFiles, false);

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(user.getUserId())
                    .parseMode(MARKDOWNV2)
                    .text(text
                            .replace(".", "\\.")
                            .replace("-", "\\-")
                    )
                    .replyMarkup(inlineKeyboardMarkup)
                    .build();
            return List.of(sendMessage);
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            user = userService.findOrCreate(callbackQuery.getFrom().getId().toString());

            List<BotApiMethod<?>> response = new ArrayList<>();
            String data = callbackQuery.getData();
            if (data.equals("back")) {

            } else if (data.equals("next")) {

            } else if (data.equals("get_link")) {

            } else if (data.equals("get_public_link")) {

            } else if (data.equals("delete_public_link")) {

            } else if (data.equals("delete_file")) {
                deleteFilesButton(user, callbackQuery, response);
            } else if (data.equals("del_back")) {
                deleteBackButton(user, callbackQuery, response);
            } else if (data.startsWith("del*")) {
                deleteFileButton(user, callbackQuery, response);
            }
            return response;
        }
        return List.of();
    }

    private void deleteFileButton(User user, CallbackQuery callbackQuery, List<BotApiMethod<?>> response) {
        String data = callbackQuery.getData();
        String[] split = data.split("\\*");
        DiskType diskType = DiskType.valueOf(split[1]);
        Token token = user.getTokens().stream()
                .filter(tkn -> diskType.equals(tkn.getDiskType()))
                .findFirst()
                .get();
        String filePath = split[2];
        aggregatorClient.deleteFile(token, filePath);
        deleteFilesButton(user, callbackQuery, response);
    }

    private String buildText(List<CloudFileDto> allFiles, boolean forDelete) {
        StringBuilder sb = new StringBuilder();
        if (forDelete) {
            sb.append("❌ Выбери файт для удаления:\n\n");
        }
        int i = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        for (CloudFileDto file : allFiles) {
            double sizeInGb = file.getSize() / 1024.0 / 1024.0 / 1024.0;
            sb.append("%d. `%s` - %s Гб \n".formatted(++i, file.getName(), decimalFormat.format(sizeInGb)));
        }
        return sb.toString();
    }

    @SneakyThrows
    private void deleteFilesButton(User user, CallbackQuery callbackQuery, List<BotApiMethod<?>> response) {
        Message message = callbackQuery.getMessage();

        List<CloudFileDto> allFiles = aggregatorClient.getAllFiles(user.getTokens());
        String text = buildText(allFiles, true);
        InlineKeyboardMarkup inlineKeyboard = buildDeleteInlineKeyboard(allFiles);
        EditMessageText editedMessage = EditMessageText.builder()
                .chatId(message.getChatId().toString())
                .inlineMessageId(callbackQuery.getInlineMessageId())
                .messageId(message.getMessageId())
                .parseMode(MARKDOWNV2)
                .text(text.replace(".", "\\.")
                        .replace("-", "\\-"))
                .replyMarkup(inlineKeyboard)
                .build();
        response.add(editedMessage);
    }

    private InlineKeyboardMarkup buildDeleteInlineKeyboard(List<CloudFileDto> allFiles) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        int i = 0;
        for (CloudFileDto file : allFiles) {
            builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text("%d. %s".formatted(++i, file.getName()))
                    .callbackData("del*%s*%s".formatted(file.getDiskType(), file.getPath()))
                    .build()));
        }
        builder.keyboardRow(List.of(InlineKeyboardButton.builder()
                .text("⬅️ Назад")
                .callbackData("del_back")
                .build()));
        return builder.build();
    }

    @SneakyThrows
    private void deleteBackButton(User user, CallbackQuery callbackQuery, List<BotApiMethod<?>> response) {
        Message message = callbackQuery.getMessage();
        List<CloudFileDto> allFiles = aggregatorClient.getAllFiles(user.getTokens());
        String text = buildText(allFiles, false);
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .parseMode(MARKDOWNV2)
                .text(text
                        .replace(".", "\\.")
                        .replace("-", "\\-")
                )
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        response.add(editMessageText);
    }

    private final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder()
                            .text("⬅️ Назад")
                            .callbackData("back")
                            .build(),
                    InlineKeyboardButton.builder()
                            .callbackData("next")
                            .text("Дальше ➡️")
                            .build()))
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder()
                            .text("🔗 Получить ссылку на файл")
                            .callbackData("get_link")
                            .build()))
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder()
                            .text("🔗 Получить публичную ссылку на файл")
                            .callbackData("get_public_link")
                            .build()))
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder()
                            .text("❌🔗 Удалить публичную ссылку на файл")
                            .callbackData("delete_public_link")
                            .build()))
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder()
                            .text("❌📄 Удалить файл")
                            .callbackData("delete_file")
                            .build()))
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder()
                            .text("📄 Отправить файл в чат")
                            .callbackData("download_file")
                            .build()))

            .build();
}
