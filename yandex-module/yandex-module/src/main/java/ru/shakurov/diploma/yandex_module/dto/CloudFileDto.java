package ru.shakurov.diploma.yandex_module.dto;

import lombok.Data;
import ru.shakurov.diploma.yandex_module.entity.DiskType;

@Data
public class CloudFileDto {
    private String name;

    private String path;

    private String publicUrl;

    private DiskType diskType;

    private Long size;
}
