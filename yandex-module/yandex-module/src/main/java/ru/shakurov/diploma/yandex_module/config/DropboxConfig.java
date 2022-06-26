package ru.shakurov.diploma.yandex_module.config;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxConfig {

    @Value("${dropbox.app_key}")
    private String appKey;
    @Value("${dropbox.app_secret}")
    private String appSecret;

    @Bean
    public DbxWebAuth dbxWebAuth() {
        return new DbxWebAuth(dbxRequestConfig(), new DbxAppInfo(appKey, appSecret));
    }

    @Bean
    public DbxRequestConfig dbxRequestConfig() {
        return new DbxRequestConfig("CloudDriveAggregator");
    }
}
