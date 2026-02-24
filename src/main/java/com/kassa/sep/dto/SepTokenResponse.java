package com.kassa.sep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response from the SEP gateway for a token request.
 * Can represent both success and error responses.
 * <p>
 * Success response: status = 1, token is present
 * Error response: status != 1 (typically -1), errorCode and errorDesc are present
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SepTokenResponse {

  /** Status code; 1 indicates success, -1 or other values indicate failure. */
  private Integer status;

  /** Payment token to use in the gateway redirect URL (present only on success). */
  private String token;

  /** Error code string (present only on error, e.g. "5", "10"). */
  @JsonProperty("errorCode")
  private String errorCode;

  /** Error description in Persian (present only on error). */
  @JsonProperty("errorDesc")
  private String errorDesc;

  /**
   * Checks if the response indicates success.
   *
   * @return true if status is 1, false otherwise
   */
  public boolean isSuccess() {
    return Integer.valueOf(1).equals(status);
  }

  /**
   * Gets the error code as an enum, if available.
   *
   * @return SepErrorCode enum value, or null if errorCode is not set or not recognized
   */
  public SepErrorCode getErrorCodeEnum() {
    return errorCode != null ? SepErrorCode.fromCode(errorCode) : null;
  }
}
