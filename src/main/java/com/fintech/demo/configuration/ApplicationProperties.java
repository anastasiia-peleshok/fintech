package com.fintech.demo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("fintech")
public class ApplicationProperties {
    private double commissionPercent;
}
