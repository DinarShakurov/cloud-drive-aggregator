package ru.shakurov.diploma.yandex_module.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.shakurov.diploma.yandex_module.component.ObjectToUrlEncodedConverter;

@Configuration
public class ApplicationConfiguration {

    @Bean("basicRestTemplate")
    public RestTemplate restTemplate() {

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new ObjectToUrlEncodedConverter(new ObjectMapper()));
        return template;
    }


}
