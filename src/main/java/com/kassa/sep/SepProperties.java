package com.kassa.sep;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the SEP (Shaparak Electronic Payment) gateway.
 * Bound to the {@code sep} prefix in application configuration (e.g. {@code sep.base-url}, {@code sep.terminal-id}).
 */
@Configuration
@ConfigurationProperties(prefix = "sep")
public record SepProperties(
  /** SEP gateway base URL. Defaults to the official Shaparak SEP endpoint. */
  @DefaultValue("https://sep.shaparak.ir")
  String baseUrl,
  /** Terminal identifier assigned by the payment gateway. */
  String terminalId) {
}
