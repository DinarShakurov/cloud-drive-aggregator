package ru.shakurov.diploma.yandex_module.service;

import ru.shakurov.diploma.yandex_module.entity.DiskType;

public interface TokenFacade {

    void handleAuth(DiskType diskType, String userId, String code);
}
