package ru.shakurov.diploma.yandex_module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class YandexTokenResponse implements TokenResponse {

    /**
     * Тип выданного токена. Всегда принимает значение «bearer».
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * OAuth-токен с запрошенными правами или с правами, указанными при регистрации приложения.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * Время жизни токена в секундах.
     */
    @JsonProperty("expires_in")
    private long expiresIn;

    /**
     * Токен, который можно использовать для продления срока жизни соответствующего OAuth-токена.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * Права, запрошенные разработчиком или указанные при регистрации приложения. Поле scope является дополнительным и возвращается, если OAuth предоставил токен с меньшим набором прав, чем было запрошено.
     */
    private String scope;

    private final long issueTime = System.currentTimeMillis();

    /**
     * @return Expiration time in seconds
     */
    @Override
    public long getExpiresAt() {
        return TimeUnit.MILLISECONDS.toSeconds(issueTime + TimeUnit.SECONDS.toMillis(expiresIn));
    }
}
