package ru.shakurov.diploma.yandex_module.service;

import ru.shakurov.diploma.yandex_module.entity.User;

public interface UserService {

    User findOrCreate(String userId);


}
