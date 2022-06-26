package ru.shakurov.diploma.yandex_module.client;

import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.Link;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.json.ResourceList;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.File;
import ru.shakurov.diploma.yandex_module.dto.CloudFileDto;
import ru.shakurov.diploma.yandex_module.dto.DiskInfoDto;
import ru.shakurov.diploma.yandex_module.dto.TokenResponse;
import ru.shakurov.diploma.yandex_module.dto.YandexTokenRequest;
import ru.shakurov.diploma.yandex_module.dto.YandexTokenResponse;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.entity.Token;
import ru.shakurov.diploma.yandex_module.helper.YandexHelper;
import ru.shakurov.diploma.yandex_module.mapper.YandexMapper;

import java.util.List;

import static java.util.Optional.ofNullable;
import static ru.shakurov.diploma.yandex_module.helper.YandexHelper.buildClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class YandexApiClient implements ApiClient {

    @Value("${yandex.authorization}")
    private String yandexAuthorization;
    private final RestTemplate restTemplate;
    private final YandexMapper mapper;

    @Override
    public DiskType getDiskType() {
        return DiskType.YANDEX;
    }

    @Override
    public TokenResponse getToken(String code) {
        log.info("[Yandex] Getting token for code {}", code);

        HttpHeaders httpHeaders = buildDefaultHeaders();
        HttpEntity<YandexTokenRequest> entity = new HttpEntity<>(new YandexTokenRequest(code), httpHeaders);

        YandexTokenResponse response = ofNullable(
                restTemplate.postForObject("https://oauth.yandex.ru/token", entity, YandexTokenResponse.class)
        ).orElseThrow();

        log.info("[Yandex] Token response: {}", response);
        return response;
    }

    private HttpHeaders buildDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(yandexAuthorization);
        return headers;
    }

    @SneakyThrows
    @Override
    public void uploadFile(Token token, File file) {
    }

    @SneakyThrows
    public Link uploadFile(Token token, String url) {
        RestClient restClient = buildClient(token.getAccessToken());
        return restClient.saveFromUrl(url, "disk:/File_2.txt");
    }

    @SneakyThrows
    @Override
    public List<CloudFileDto> getFiles(Token token) {
        RestClient restClient = buildClient(token.getAccessToken());
        ResourcesArgs root = new ResourcesArgs.Builder().setPath("disk:/").build();
        Resource resources = restClient.getResources(root);
        ResourceList resourceList = resources.getResourceList();
        List<Resource> items = resourceList.getItems();
        return items.stream().map(mapper::mapResource).toList();
    }

    @SneakyThrows
    @Override
    public void delete(Token token, String filePath) {
        RestClient restClient = buildClient(token.getAccessToken());
        restClient.delete(filePath, true);
    }

    @SneakyThrows
    @Override
    public DiskInfoDto getDiskInfo(Token token) {
        RestClient restClient = buildClient(token.getAccessToken());
        DiskInfo diskInfo = restClient.getDiskInfo();
        return mapper.map(diskInfo);
    }

    @SneakyThrows
    @Override
    public String buildPublicUrl(Token token) {
        RestClient restClient = buildClient(token.getAccessToken());
        return null;
    }
}
