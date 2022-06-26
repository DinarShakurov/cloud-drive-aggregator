package ru.shakurov.diploma.yandex_module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class YandexTokenRequest {

    @JsonProperty("grant_type")
    private final String grantType = "authorization_code";

    @JsonProperty("code")
    private final String code;

    public YandexTokenRequest(String code) {
        this.code = code;
    }
}
