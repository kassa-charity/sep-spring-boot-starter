package com.kassa.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Transaction detail nested in the verify transaction response.
 * Contains the verified transaction data from the gateway.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SepTransactionDetail {

  /** Retrieval Reference Number (RRN) from the bank. */
  @JsonProperty("RRN")
  private String rrn;

  /** Reference number from the gateway. */
  @JsonProperty("RefNum")
  private String refNum;

  /** Masked card number (e.g. "621986****8080"). */
  @JsonProperty("MaskedPan")
  private String maskedPan;

  /** Hashed card number (PAN). */
  @JsonProperty("HashedPan")
  private String hashedPan;

  /** Terminal number. */
  @JsonProperty("TerminalNumber")
  private Integer terminalNumber;

  /** Original transaction amount in Rials. */
  @JsonProperty("OrginalAmount")
  private Long orginalAmount;

  /** Effective transaction amount in Rials. */
  @JsonProperty("AffectiveAmount")
  private Long affectiveAmount;

  /** Trace date from the gateway (e.g. "2019-09-16 18:11:06"). */
  @JsonProperty("StraceDate")
  private String straceDate;

  /** Trace number. */
  @JsonProperty("StraceNo")
  private String straceNo;
}
