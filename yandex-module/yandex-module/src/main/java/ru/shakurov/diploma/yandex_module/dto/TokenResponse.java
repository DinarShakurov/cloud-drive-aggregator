package ru.shakurov.diploma.yandex_module.dto;

import javax.annotation.Nullable;

public interface TokenResponse {

    String getAccessToken();

    String getRefreshToken();

    long getExpiresAt();

    @Nullable
    String getScope();
}
