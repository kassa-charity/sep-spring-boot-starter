package com.kassa.sep;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Boot auto-configuration for the SEP (Shaparak Electronic Payment) integration.
 * Registers {@link SepProperties} and exposes a {@link SepClient} bean for calling the SEP gateway API.
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(SepProperties.class)
public class SepConfiguration {
  private final SepProperties properties;

  /**
   * Creates the configuration with the given SEP properties.
   *
   * @param properties the SEP configuration properties (e.g. base URL, terminal ID)
   */
  public SepConfiguration(SepProperties properties) {
    this.properties = properties;
  }

  /**
   * Defines the {@link SepClient} bean used to perform SEP gateway operations.
   *
   * @param restClient the configured RestClient (base URL set from {@link SepProperties#baseUrl()})
   * @return the SEP client instance
   */
  @Bean
  public SepClient sepClient(RestClient restClient) {
    return new SepClient(restClient, properties);
  }
}
