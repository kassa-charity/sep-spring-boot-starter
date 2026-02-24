package com.kassa.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body for the SEP verify transaction API.
 * Used to verify a payment after the user returns from the gateway.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SepVerifyTransactionRequest {

  /** Reference number returned by the gateway after payment. */
  @JsonProperty("RefNum")
  private String refNum;

  /** Merchant terminal number. */
  @JsonProperty("TerminalNumber")
  private Integer terminalNumber;
}
