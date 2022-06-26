package ru.shakurov.diploma.yandex_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;
import ru.shakurov.diploma.yandex_module.entity.User;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findFirstByUserAndDiskTypeOrderByExpiredAtDesc(User user, DiskType diskType);
}
