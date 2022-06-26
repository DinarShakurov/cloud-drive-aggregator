package ru.shakurov.diploma.yandex_module.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleHelper {
    @Value("${google.client_id}")
    private String clientId;

    public static Object buildClient() {
        return null;
    }

    public String generateOAuthLink(String tgClientId) {
        String REDIRECT_URL = "http://localhost:8080/token/google";
        return UriComponentsBuilder.fromUriString("https://drive.google.com")
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("state", tgClientId)
                .queryParam("redirect_uri", URLEncoder.encode(REDIRECT_URL, StandardCharsets.UTF_8))
                .build()
                .toString();
    }
}
