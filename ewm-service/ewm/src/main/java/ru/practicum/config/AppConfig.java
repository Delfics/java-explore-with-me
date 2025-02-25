package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.HttpClientStats;

@Configuration
public class AppConfig {
    @Value("${url}")
    private String url;

    @Bean
    public HttpClientStats getHttpClientStatsBean() {
        return new HttpClientStats(url);
    }
}
