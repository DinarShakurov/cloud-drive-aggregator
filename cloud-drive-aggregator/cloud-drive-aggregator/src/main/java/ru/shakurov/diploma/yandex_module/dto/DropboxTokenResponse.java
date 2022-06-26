package ru.shakurov.diploma.yandex_module.dto;

import lombok.Data;

@Data
public class DropboxTokenResponse implements TokenResponse {

    private String accessToken;

    private String refreshToken;

    private long expiresAt;

    private String scope;
}
