package ru.shakurov.diploma.yandex_module.client;

import org.telegram.telegrambots.meta.api.objects.File;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.dto.DiskInfoDto;
import ru.shakurov.diploma.yandex_module.dto.TokenResponse;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;

import java.util.List;

public interface ApiClient {

    DiskType getDiskType();

    TokenResponse getToken(String code);

    void uploadFile(Token token, File file) throws Exception;

    List<CloudFileDto> getFiles(Token token);

    void delete(Token token, String filePath);

    DiskInfoDto getDiskInfo(Token token);

    String buildPublicUrl(Token token);
}
