package com.kassa.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request body for the SEP online payment gateway "token" action.
 * Used to obtain a payment token before redirecting the user to the gateway.
 *
 * @see <a href="https://sep.shaparak.ir">SEP Gateway</a>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SepTokenRequest {

  /** Action type; must be "token" for token request. */
  @JsonProperty("action")
  private String action;

  /** Merchant terminal identifier assigned by the bank. */
  @JsonProperty("TerminalId")
  private String terminalId;

  /** Transaction amount in Rials. */
  @JsonProperty("Amount")
  private Long amount;

  /** Merchant-generated reservation/reference number (unique per transaction). */
  @JsonProperty("ResNum")
  private String resNum;

  /** URL to which the user is redirected after payment (success or failure). */
  @JsonProperty("RedirectUrl")
  private String redirectUrl;

  /** Payer mobile number (optional, e.g. "9120000000"). */
  @JsonProperty("CellNumber")
  private String cellNumber;
}
