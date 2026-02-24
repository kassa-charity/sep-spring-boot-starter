package com.kassa.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response from the SEP gateway for transaction verification/confirmation.
 * Contains transaction details including status, reference numbers, and payment information.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SepTransactionResponse {

  /** Merchant ID. */
  @JsonProperty("MID")
  private Long mid;

  /** Terminal ID. */
  @JsonProperty("TerminalId")
  private Long terminalId;

  /** Transaction state (e.g. "OK", "Failed"). */
  @JsonProperty("State")
  private String state;

  /** Transaction status code (2 = OK, 3 = Failed, etc.). */
  @JsonProperty("Status")
  private Integer status;

  /** Retrieval Reference Number (RRN) from the bank. */
  @JsonProperty("RRN")
  private Long rrn;

  /** Reference number from the gateway. */
  @JsonProperty("RefNum")
  private String refNum;

  /** Merchant reservation/reference number (same as ResNum sent in request). */
  @JsonProperty("ResNum")
  private String resNum;

  /** Trace number. */
  @JsonProperty("TraceNo")
  private Long traceNo;

  /** Transaction amount in Rials. */
  @JsonProperty("Amount")
  private Long amount;

  /** Transaction fee/wage in Rials. */
  @JsonProperty("Wage")
  private Long wage;

  /** Masked card number (e.g. "621986****1234"). */
  @JsonProperty("SecurePan")
  private String securePan;

  /** Hashed card number. */
  @JsonProperty("HashedCardNumber")
  private String hashedCardNumber;

  /**
   * Checks if the transaction was successful.
   * Status 2 indicates success (OK).
   *
   * @return true if status is 2, false otherwise
   */
  public boolean isSuccess() {
    return Integer.valueOf(2).equals(status);
  }
}
