package ru.shakurov.diploma.yandex_module.command.impls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.shakurov.diploma.yandex_module.client.AggregatorClient;
import ru.shakurov.diploma.yandex_module.command.CommandHandler;
import ru.shakurov.diploma.yandex_module.command.CommandType;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.dto.DiskInfoDto;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.User;
import ru.shakurov.diploma.yandex_module.service.UserService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListCommandHandler implements CommandHandler {

    private final UserService userService;
    private final AggregatorClient aggregatorClient;

    @Override
    public CommandType getCommandType() {
        return CommandType.LIST;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BotApiMethod<?>> handleCommand(Update update) {
        String clientId = update.getMessage().getFrom().getId().toString();
        User user = userService.findOrCreate(clientId);

        Map<DiskType, List<CloudFileDto>> allFiles = aggregatorClient.getAllFiles(user.getTokens())
                .stream()
                .collect(Collectors.groupingBy(CloudFileDto::getDiskType));
        List<DiskInfoDto> disksInfo = aggregatorClient.getDisksInfo(user.getTokens());

        String text = buildText(allFiles, disksInfo);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getUserId())
                .parseMode(ParseMode.MARKDOWNV2)
                .text(text)
                .build();
        return List.of(sendMessage);
    }

    private String buildText(Map<DiskType, List<CloudFileDto>> allFiles, List<DiskInfoDto> disksInfo) {
        StringBuilder sb = new StringBuilder();
        double total = 0;
        double free = 0;
        for (DiskInfoDto diskInfoDto : disksInfo) {
            total += diskInfoDto.getTotalSpace();
            free += diskInfoDto.getFreeSpace();
        }
        total = total / 1024 / 1024 / 1024;
        free = free / 1024 / 1024 / 1024;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        sb.append("💾 *Всего свободно %s Гб из %s Гб.*\n\n"
                .formatted(
                        decimalFormat.format(free), decimalFormat.format(total)
                )
        );

        for (DiskInfoDto diskInfoDto : disksInfo) {
            List<CloudFileDto> cloudFiles = allFiles.getOrDefault(diskInfoDto.getDiskType(), new ArrayList<>());
            total = diskInfoDto.getTotalSpace() / 1024.0 / 1024.0 / 1024.0;
            free = diskInfoDto.getFreeSpace() / 1024.0 / 1024.0 / 1024.0;
            sb.append("💿 %s (свободно %s Гб из %s Гб)\n"
                    .formatted(diskInfoDto.getName(), decimalFormat.format(free), decimalFormat.format(total))
            );
            int i = 0;
            for (CloudFileDto cloudFile : cloudFiles) {
                total = cloudFile.getSize() / 1024.0 / 1024.0 / 1024.0;
                sb.append("\t%d. `%s` - %s Гб\n".formatted(++i, cloudFile.getName(), decimalFormat.format(total)));
            }
            sb.append("\n");
        }
        return sb.toString()
                .replace(".", "\\.")
                .replace(")", "\\)")
                .replace("(", "\\(")
                .replace("-", "\\-");
    }
}
