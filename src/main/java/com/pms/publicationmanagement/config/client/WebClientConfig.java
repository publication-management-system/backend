package com.pms.publicationmanagement.config.client;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Objects;


@Configuration
public class WebClientConfig {

    @Value("${scraping.service.base-url}")
    private String scrapingServiceBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }

    @Bean
    public WebClient scrapingServiceWebClient() {
        return Objects.requireNonNull(WebClient.builder()
                        .baseUrl(scrapingServiceBaseUrl))
                .build();
    }

}
