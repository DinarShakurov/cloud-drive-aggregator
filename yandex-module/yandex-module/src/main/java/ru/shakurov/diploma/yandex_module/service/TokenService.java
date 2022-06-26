package ru.shakurov.diploma.yandex_module.service;

import ru.shakurov.diploma.yandex_module.dto.TokenResponse;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;

public interface TokenService {

    void saveToken(TokenResponse response, String userId, DiskType diskType);

    Token getToken(String userId, DiskType diskType);
}
