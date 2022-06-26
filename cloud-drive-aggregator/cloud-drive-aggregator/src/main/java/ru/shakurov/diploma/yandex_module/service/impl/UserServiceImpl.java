package ru.shakurov.diploma.yandex_module.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shakurov.diploma.yandex_module.entity.User;
import ru.shakurov.diploma.yandex_module.repository.UserRepository;
import ru.shakurov.diploma.yandex_module.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User findOrCreate(String userId) {
        return userRepository.findById(userId).orElseGet(() -> {
            User user = new User();
            user.setUserId(userId);
            return userRepository.save(user);
        });
    }
}
