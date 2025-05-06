package com.pms.publicationmanagement.config.scraping;

import com.microsoft.playwright.Playwright;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "scraping-config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapingConfig {
    private boolean showBrowser;

    private List<String> browserArgs;
}
