package ru.shakurov.diploma.yandex_module.mapper;

import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.dto.DiskInfoDto;

@Mapper(componentModel = "spring")
public interface YandexMapper {

    @Mapping(target = "diskType", expression = "java(ru.shakurov.diploma.yandex_module.entity.DiskType.YANDEX)")
    @Mapping(target = "path", source = "path.path")
    CloudFileDto mapResource(Resource resource);

    @Mapping(target = "diskType", expression = "java(ru.shakurov.diploma.yandex_module.entity.DiskType.YANDEX)")
    @Mapping(target = "name", constant = "Яндекс.Диск")
    @Mapping(target = "freeSpace", expression = "java(diskInfo.getTotalSpace() - diskInfo.getUsedSpace())")
    DiskInfoDto map(DiskInfo diskInfo);
}
