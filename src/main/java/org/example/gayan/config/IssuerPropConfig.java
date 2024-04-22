package org.example.gayan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/22/24
 * Time: 6:10 PM
 */
@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Getter
@Setter
public class IssuerPropConfig {
    private List<String> issuers;
}
