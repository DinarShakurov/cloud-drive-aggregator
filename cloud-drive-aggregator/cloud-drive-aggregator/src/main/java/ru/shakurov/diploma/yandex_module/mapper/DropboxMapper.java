package ru.shakurov.diploma.yandex_module.mapper;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.SpaceUsage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.dto.DiskInfoDto;
import ru.shakurov.diploma.yandex_module.dto.DropboxTokenResponse;

@Mapper(componentModel = "spring")
public interface DropboxMapper {


    @Mapping(target = "expiresAt", expression = "java(java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(authFinish.getExpiresAt()))")
    DropboxTokenResponse map(DbxAuthFinish authFinish);

    @Mapping(target = "publicUrl", ignore = true)
    @Mapping(target = "path", source = "pathDisplay")
    @Mapping(target = "diskType", expression = "java(ru.shakurov.diploma.yandex_module.entity.DiskType.DROPBOX)")
    CloudFileDto map(FileMetadata metadata);

    @Mapping(target = "usedSpace", source = "used")
    @Mapping(target = "totalSpace", source = "allocation.individualValue.allocated")
    @Mapping(target = "name", constant = "Dropbox")
    @Mapping(target = "freeSpace", expression = "java(spaceUsage.getAllocation().getIndividualValue().getAllocated() - spaceUsage.getUsed())")
    @Mapping(target = "diskType", expression = "java(ru.shakurov.diploma.yandex_module.entity.DiskType.DROPBOX)")
    DiskInfoDto map(SpaceUsage spaceUsage);
}
