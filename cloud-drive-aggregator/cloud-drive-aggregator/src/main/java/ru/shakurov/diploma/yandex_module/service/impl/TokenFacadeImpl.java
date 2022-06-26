package ru.shakurov.diploma.yandex_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.shakurov.diploma.yandex_module.client.ApiClient;
import ru.shakurov.diploma.yandex_module.dto.NewAuthEvent;
import ru.shakurov.diploma.yandex_module.dto.TokenResponse;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.service.TokenFacade;
import ru.shakurov.diploma.yandex_module.service.TokenService;

@Service
@RequiredArgsConstructor
@Slf4j
class TokenFacadeImpl implements TokenFacade {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final TokenService tokenService;
    private final ApiClient yandexApiClient;
    private final ApiClient dropboxApiClient;
    private final ApiClient googleApiClient;

    @Override
    public void handleAuth(DiskType diskType, String userId, String code) {
        TokenResponse tokenResponse = switch (diskType) {
            case YANDEX -> yandexApiClient.getToken(code);
            case GOOGLE -> googleApiClient.getToken(code);
            case DROPBOX -> dropboxApiClient.getToken(code);
        };
        tokenService.saveToken(tokenResponse, userId, diskType);
        applicationEventPublisher.publishEvent(new NewAuthEvent(this, userId, diskType));
    }
}
