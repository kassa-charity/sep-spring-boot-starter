package com.kassa.sep.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Error codes returned by the SEP gateway.
 * These codes indicate various failure scenarios during payment processing.
 */
@Getter
@RequiredArgsConstructor
public enum SepErrorCode {
  /** User canceled the payment. */
  CANCELED_BY_USER("-", "CanceledByUser", "کاربر انصراف داده است"),

  /** Payment completed successfully. */
  OK("2", "OK", "پرداخت با موفقیت انجام شد"),

  /** Payment failed. */
  FAILED("3", "Failed", "پرداخت انجام نشد"),

  /** User did not respond within the specified time window. */
  SESSION_IS_NULL("4", "SessionIsNull", "کاربر در بازه زمانی تعیین شده پاسخی ارسال نکرده است"),

  /** Invalid parameters sent. */
  INVALID_PARAMETERS("5", "InvalidParameters", "پارامترهای ارسالی نامعتبر است"),

  /** Merchant IP address is invalid for token-based payments. */
  MERCHANT_IP_ADDRESS_IS_INVALID("8", "MerchantIpAddressIsInvalid", "در پرداخت های بر پایه توکن آدرس سرور پذیرنده نامعتبر است"),

  /** Token not found. */
  TOKEN_NOT_FOUND("10", "TokenNotFound", "توکن ارسال شده یافت نشد"),

  /** Only token-based transactions are allowed for this terminal. */
  TOKEN_REQUIRED("11", "TokenRequired", "با این شماره ترمینال فقط تراکنش های توکنی قابل پرداخت هستند"),

  /** Terminal number not found. */
  TERMINAL_NOT_FOUND("12", "TerminalNotFound", "شماره ترمینال ارسال شده یافت نشد"),

  /** Multi-settle policy restrictions not met. */
  MULTISETTLE_POLICY_ERRORS("21", "MultisettlePolicyErrors", "محدودیت های مدل چند حسابی رعایت نشده");

  private final String code;
  private final String englishName;
  private final String persianDescription;

  /**
   * Finds an error code by its string code value.
   *
   * @param code the error code string
   * @return the matching SepErrorCode, or null if not found
   */
  public static SepErrorCode fromCode(String code) {
    if (code == null) {
      return null;
    }
    for (SepErrorCode errorCode : values()) {
      if (errorCode.code.equals(code)) {
        return errorCode;
      }
    }
    return null;
  }
}
