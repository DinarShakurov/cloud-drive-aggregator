package ru.shakurov.diploma.yandex_module.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.shakurov.diploma.yandex_module.entity.DiskType;

@Getter
public class FileUploadedEvent extends ApplicationEvent {
    private final String clientId;
    private final String replyToId;

    public FileUploadedEvent(Object source, String clientId, String replyToId) {
        super(source);
        this.clientId = clientId;
        this.replyToId = replyToId;
    }
}
