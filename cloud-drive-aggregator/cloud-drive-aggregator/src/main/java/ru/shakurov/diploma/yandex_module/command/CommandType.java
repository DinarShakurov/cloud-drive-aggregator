package ru.shakurov.diploma.yandex_module.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandType {
    START("/start", "Начало работы с ботом"),
    CONNECT("/connect", "Получить ссылки для подключения к облачным дискам"),

    LIST("/list", "Получить список файлов"),

    ALL("/all", "Получить список файлов с возможностью взаимодействия"),
    DOCUMENT("", "Загрузить файл на диск");

    private final String command;
    private final String description;

}
