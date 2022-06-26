package ru.shakurov.diploma.yandex_module.dto;

import lombok.Data;
import ru.shakurov.diploma.yandex_module.entity.DiskType;

@Data
public class DiskInfoDto {
    private String name;
    private long totalSpace;
    private long freeSpace;
    private long usedSpace;
    private DiskType diskType;
}
