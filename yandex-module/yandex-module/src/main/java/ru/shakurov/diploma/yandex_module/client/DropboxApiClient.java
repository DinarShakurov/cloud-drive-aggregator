package ru.shakurov.diploma.yandex_module.client;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.RetryException;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CommitInfo;
import com.dropbox.core.v2.files.DeleteResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetTemporaryLinkResult;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.UploadSessionCursor;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.users.SpaceUsage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.File;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.dto.DiskInfoDto;
import ru.shakurov.diploma.yandex_module.dto.TokenResponse;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;
import ru.shakurov.diploma.yandex_module.helper.DropboxHelper;
import ru.shakurov.diploma.yandex_module.mapper.DropboxMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class DropboxApiClient implements ApiClient {

    @Value("${bot.token}")
    private String botToken;

    private final DropboxHelper dropboxHelper;
    private final DropboxMapper dropboxMapper;
    private final DbxWebAuth dbxWebAuth;

    @Value("${file.path}")
    private String filePath;
    private static final long CHUNKED_UPLOAD_CHUNK_SIZE = 100L << 20;

    @Override
    public DiskType getDiskType() {
        return DiskType.DROPBOX;
    }

    @Override
    public TokenResponse getToken(String code) {
        try {
            DbxAuthFinish authFinish = dbxWebAuth.finishFromCode(code, "http://localhost:8080/token/dropbox");
            return dropboxMapper.map(authFinish);
        } catch (DbxException e) {
            log.error("[Dropbox] Error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadFile(Token token, File file) throws IOException, DbxException {
        DbxClientV2 dbxClient = dropboxHelper.buildClient(token);
        if (file == null) {
            java.io.File lclFile = new java.io.File(filePath);
            long size = lclFile.length();
            long uploaded = 0L;
            for (int i = 0; i < 10; ++i) {
                if (i > 0) {
                    System.out.printf("Retrying chunked upload (%d / %d attempts)\n", i + 1, 10);
                }

                IOUtil.ProgressListener progressListener = new IOUtil.ProgressListener() {
                    long uploadedBytes = 0;

                    @Override
                    public void onProgress(long l) {
                        printProgress(l + uploadedBytes, size);
                        if (l == CHUNKED_UPLOAD_CHUNK_SIZE) uploadedBytes += CHUNKED_UPLOAD_CHUNK_SIZE;
                    }
                };
                String sessionId = null;
                try (InputStream in = new FileInputStream(lclFile)) {
                    in.skip(uploaded);
                    sessionId = dbxClient.files().uploadSessionStart()
                            .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE, progressListener)
                            .getSessionId();
                    uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                    printProgress(uploaded, size);

                    UploadSessionCursor cursor = new UploadSessionCursor(sessionId, uploaded);
                    while ((size - uploaded) > CHUNKED_UPLOAD_CHUNK_SIZE) {
                        dbxClient.files().uploadSessionAppendV2(cursor)
                                .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE, progressListener);
                        uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                        printProgress(uploaded, size);
                        cursor = new UploadSessionCursor(sessionId, uploaded);
                    }
                    long remaining = size - uploaded;
                    CommitInfo commitInfo = CommitInfo.newBuilder("/File_5.txt")
                            .withMode(WriteMode.ADD)
                            .withClientModified(new Date())
                            .build();
                    FileMetadata metadata = dbxClient.files().uploadSessionFinish(cursor, commitInfo)
                            .uploadAndFinish(in, remaining, progressListener);


                    return;
                } catch (RetryException | NetworkIOException e) {
                    log.error("", e);
                }
            }
        } else {
            String urlFrom = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();
            try (InputStream in = new URL(urlFrom).openStream()) {
                String filename = file.getFilePath().split("/")[1];

                dbxClient.files()
                        .uploadBuilder("/" + filename)
                        .withMode(WriteMode.ADD)
                        .withClientModified(new Date())
                        .uploadAndFinish(in);
            } catch (DbxException | IOException e) {
                log.error("[DROPBOX] file \"{}\"upload error: {}",
                        file.getFilePath(), e.getCause());
                throw e;
            }
        }
    }

    private static void printProgress(long uploaded, long size) {
        System.out.printf("Uploaded %12d / %12d bytes (%5.2f%%)\n", uploaded, size, 100 * (uploaded / (double) size));
    }

    @SneakyThrows
    @Override
    public List<CloudFileDto> getFiles(Token token) {
        DbxClientV2 dbxClient = dropboxHelper.buildClient(token);
        String root = "";
        ListFolderResult listFolderResult = dbxClient.files().listFolder(root);
        return listFolderResult.getEntries()
                .stream()
                .map(metadata -> (FileMetadata) metadata)
                .map(dropboxMapper::map)
                .toList();
    }

    @SneakyThrows
    @Override
    public void delete(Token token, String filePath) {
        DbxClientV2 dbxClientV2 = dropboxHelper.buildClient(token);
        DeleteResult deleteResult = dbxClientV2.files().deleteV2(filePath);
    }

    @SneakyThrows
    @Override
    public DiskInfoDto getDiskInfo(Token token) {
        DbxClientV2 dbxClientV2 = dropboxHelper.buildClient(token);
        SpaceUsage spaceUsage = dbxClientV2.users().getSpaceUsage();
        return dropboxMapper.map(spaceUsage);
    }

    @SneakyThrows
    @Override
    public String buildPublicUrl(Token token) {
        DbxClientV2 dbxClientV2 = dropboxHelper.buildClient(token);
        GetTemporaryLinkResult temporaryLink = dbxClientV2.files().getTemporaryLink("/File_2.txt");
        return temporaryLink.getLink();
    }
}
