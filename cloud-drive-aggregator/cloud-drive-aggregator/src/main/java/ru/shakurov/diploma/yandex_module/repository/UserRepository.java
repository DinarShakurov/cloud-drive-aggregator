package ru.shakurov.diploma.yandex_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shakurov.diploma.yandex_module.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
