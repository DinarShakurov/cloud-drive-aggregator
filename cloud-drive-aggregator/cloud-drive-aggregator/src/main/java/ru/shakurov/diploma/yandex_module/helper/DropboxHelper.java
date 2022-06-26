package ru.shakurov.diploma.yandex_module.helper;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.TokenAccessType;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.shakurov.diploma.yandex_module.entity.Token;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Slf4j
public class DropboxHelper {

    @Value("${dropbox.app_key}")
    private String appKey;
    @Value("${dropbox.app_secret}")
    private String appSecret;

    public DbxClientV2 buildClient(Token token) {
        long expiresAt = token.getExpiredAt().toInstant(ZoneOffset.UTC).toEpochMilli();
        DbxRequestConfig config = DbxRequestConfig.newBuilder("CloudDriveAggregator").build();
        DbxCredential dbxCredential = new DbxCredential(token.getAccessToken(),
                expiresAt, token.getRefreshToken(), appKey, appSecret);
        return new DbxClientV2(config, dbxCredential);
    }

    public String generateOAuthLink(String tgClientId) {
        String REDIRECT_URL = "http://localhost:8080/token/dropbox";
        return UriComponentsBuilder.fromUriString("https://www.dropbox.com/oauth2/authorize")
                .queryParam("client_id", appKey)
                .queryParam("response_type", "code")
                .queryParam("token_access_type", TokenAccessType.OFFLINE) // TokenAccessType.OFFLINE means refresh_token + access_token. ONLINE means access_token only.
                .queryParam("state", tgClientId)
                .queryParam("redirect_uri", URLEncoder.encode(REDIRECT_URL, StandardCharsets.UTF_8))
                .build()
                .toString();
    }
}
