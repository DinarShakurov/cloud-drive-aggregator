package ru.shakurov.diploma.yandex_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shakurov.diploma.yandex_module.dto.TokenResponse;
import ru.shakurov.diploma.yandex_module.entity.User;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;
import ru.shakurov.diploma.yandex_module.repository.UserRepository;
import ru.shakurov.diploma.yandex_module.repository.TokenRepository;
import ru.shakurov.diploma.yandex_module.service.TokenService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@RequiredArgsConstructor
@Slf4j
class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void saveToken(TokenResponse response, String userId, DiskType diskType) {
        User user = userRepository.getReferenceById(userId);
        Token token = new Token();
        token.setAccessToken(response.getAccessToken());
        token.setRefreshToken(response.getRefreshToken());
        token.setDiskType(diskType);
        token.setUser(user);
        token.setExpiredAt(LocalDateTime.ofEpochSecond(response.getExpiresAt(), 0, ZoneOffset.UTC));
        log.info("Saving token = {}", token);
        tokenRepository.save(token);
    }

    @Override
    public Token getToken(String userId, DiskType diskType) {
        User user = userRepository.getReferenceById(userId);
        Token token = tokenRepository.findFirstByUserAndDiskTypeOrderByExpiredAtDesc(user, diskType).get();
        return token;
    }


}
