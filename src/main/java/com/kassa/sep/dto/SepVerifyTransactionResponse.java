package com.kassa.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response from the SEP verify transaction API.
 * Contains transaction details and verification result.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SepVerifyTransactionResponse {

  /** Transaction detail when verification succeeds. */
  @JsonProperty("TransactionDetail")
  private SepTransactionDetail transactionDetail;

  /** Result code; 0 indicates success. */
  @JsonProperty("ResultCode")
  private Integer resultCode;

  /** Result description (e.g. in Persian). */
  @JsonProperty("ResultDescription")
  private String resultDescription;

  /** Whether the verification was successful. */
  @JsonProperty("Success")
  private Boolean success;

  /**
   * Checks if the verification was successful.
   *
   * @return true if Success is true and ResultCode is 0
   */
  public boolean isSuccess() {
    return Boolean.TRUE.equals(success) && Integer.valueOf(0).equals(resultCode);
  }

  /**
   * Gets the result code as an enum, if available.
   *
   * @return SepResultCode enum value, or null if resultCode is not set or not recognized
   */
  public SepResultCode getResultCodeEnum() {
    return resultCode != null ? SepResultCode.fromCode(resultCode) : null;
  }
}
