package ru.shakurov.diploma.yandex_module.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.File;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.dto.DiskInfoDto;
import ru.shakurov.diploma.yandex_module.dto.TokenResponse;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleApiClient implements ApiClient {
    @Override
    public DiskType getDiskType() {
        return DiskType.GOOGLE;
    }

    @Override
    public TokenResponse getToken(String code) {
        return null;
    }

    @Override
    public void uploadFile(Token token, File file) {
        throw new NotImplementedException();
    }

    @Override
    public List<CloudFileDto> getFiles(Token token) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Token token, String filePath) {
        throw new NotImplementedException();
    }

    @Override
    public DiskInfoDto getDiskInfo(Token token) {
        throw new NotImplementedException();
    }

    @Override
    public String buildPublicUrl(Token token) {
        throw new NotImplementedException();
    }
}
