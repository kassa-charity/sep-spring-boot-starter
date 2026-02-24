package com.kassa.sep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.kassa.sep.SepProperties;

/**
 * Configures the {@link RestClient} used by the SEP integration.
 * The client is built with the base URL from {@link SepProperties} so all SEP API calls use the correct gateway endpoint.
 */
@Configuration
public class RestConfig {
  private final SepProperties properties;

  /**
   * Creates the config with the given SEP properties.
   *
   * @param properties the SEP configuration (used for {@link SepProperties#baseUrl()})
   */
  public RestConfig(SepProperties properties) {
    this.properties = properties;
  }

  /**
   * Builds and registers the RestClient bean for SEP API calls.
   *
   * @return a RestClient with base URL set from {@link SepProperties#baseUrl()}
   */
  @Bean
  public RestClient restClient() {
    return RestClient.builder()
      .baseUrl(properties.baseUrl())
      .build();
  }
}
