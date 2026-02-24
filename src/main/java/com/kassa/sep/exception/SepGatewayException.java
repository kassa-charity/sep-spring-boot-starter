package com.kassa.sep.exception;

import com.kassa.sep.dto.SepErrorCode;

import lombok.Getter;

/**
 * Exception thrown when the SEP gateway returns an error response.
 * Contains the error code and description from the gateway.
 */
@Getter
public class SepGatewayException extends RuntimeException {
  private final String errorCode;
  private final String errorDesc;
  private final SepErrorCode errorCodeEnum;

  /**
   * Creates a new SEP gateway exception.
   *
   * @param errorCode the error code string from the gateway (e.g. "5", "10")
   * @param errorDesc the error description in Persian
   */
  public SepGatewayException(String errorCode, String errorDesc) {
    super(String.format("SEP Gateway Error [%s]: %s", errorCode, errorDesc));
    this.errorCode = errorCode;
    this.errorDesc = errorDesc;
    this.errorCodeEnum = SepErrorCode.fromCode(errorCode);
  }

  /**
   * Creates a new SEP gateway exception with a custom message.
   *
   * @param message    custom error message
   * @param errorCode  the error code string from the gateway
   * @param errorDesc  the error description in Persian
   */
  public SepGatewayException(String message, String errorCode, String errorDesc) {
    super(message);
    this.errorCode = errorCode;
    this.errorDesc = errorDesc;
    this.errorCodeEnum = SepErrorCode.fromCode(errorCode);
  }
}
