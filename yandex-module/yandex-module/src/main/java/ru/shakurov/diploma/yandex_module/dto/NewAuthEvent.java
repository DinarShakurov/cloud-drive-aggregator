package ru.shakurov.diploma.yandex_module.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.shakurov.diploma.yandex_module.entity.DiskType;

@Getter
public class NewAuthEvent extends ApplicationEvent {

    private final String clientId;
    private final DiskType diskType;

    public NewAuthEvent(Object source, String clientId, DiskType diskType) {
        super(source);
        this.clientId = clientId;
        this.diskType = diskType;
    }
}
