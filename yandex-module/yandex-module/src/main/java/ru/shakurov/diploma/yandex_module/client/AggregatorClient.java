package ru.shakurov.diploma.yandex_module.client;

import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.json.Link;
import com.yandex.disk.rest.json.Operation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.File;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.dto.DiskInfoDto;
import ru.shakurov.diploma.yandex_module.dto.FileUploadedEvent;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;
import ru.shakurov.diploma.yandex_module.helper.YandexHelper;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class AggregatorClient {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<DiskType, ApiClient> clientsMap = new EnumMap<>(DiskType.class);

    @Autowired
    public void initClients(List<ApiClient> apiClients) {
        apiClients.forEach(apiClient -> clientsMap.put(apiClient.getDiskType(), apiClient));
    }

    public List<CloudFileDto> getAllFiles(List<Token> tokens) {
        return tokens.stream()
                .flatMap(token -> clientsMap.get(token.getDiskType())
                        .getFiles(token)
                        .stream())
                .toList();
    }

    public void deleteFile(Token token, String filePath) {
        clientsMap.get(token.getDiskType()).delete(token, filePath);
    }

    public List<DiskInfoDto> getDisksInfo(List<Token> tokens) {
        return tokens.stream()
                .map(token -> clientsMap.get(token.getDiskType()).getDiskInfo(token))
                .toList();
    }

    @SneakyThrows
    public void uploadFile(List<Token> tokens, File file, FileUploadedEvent event) {

        Token yToken = tokens.stream().filter(token -> DiskType.YANDEX.equals(token.getDiskType())).findFirst().get();
        Token dToken = tokens.stream().filter(token -> DiskType.DROPBOX.equals(token.getDiskType())).findFirst().get();
        YandexApiClient yandexApiClient = (YandexApiClient) clientsMap.get(DiskType.YANDEX);
        DropboxApiClient dropboxApiClient = (DropboxApiClient) clientsMap.get(DiskType.DROPBOX);
        String publicUrl = dropboxApiClient.buildPublicUrl(dToken);
        Link link = yandexApiClient.uploadFile(yToken, publicUrl);
        RestClient restClient = YandexHelper.buildClient(yToken.getAccessToken());
        while (true) {
            Operation operation = restClient.getOperation(link);
            log.info("Operation: {}", operation);
            if (!operation.isInProgress()) {
                if (operation.isSuccess()) {
                    break;
                }
                if (!operation.isSuccess()) {
                    throw new RuntimeException();
                }
            }

            Thread.sleep(2000L);
        }
        log.info("Uploading to YD success");
        dropboxApiClient.delete(dToken, "/File_2.txt");
        dropboxApiClient.uploadFile(dToken, file);
        log.info("Uploading to DB success");
        applicationEventPublisher.publishEvent(event);
    }
}
