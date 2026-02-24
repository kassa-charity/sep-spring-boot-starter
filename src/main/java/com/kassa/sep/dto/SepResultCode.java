package com.kassa.sep.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Result codes returned by the SEP verify and reverse transaction APIs.
 *
 * @see <a href="https://sep.shaparak.ir">SEP Gateway</a>
 */
@Getter
@RequiredArgsConstructor
public enum SepResultCode {
  /** Transaction not found. */
  TRANSACTION_NOT_FOUND(-2, "تراکنش یافت نشد.", "verify"),

  /** More than 30 minutes have passed since the transaction. */
  TRANSACTION_EXPIRED(-6, "بیش از نیم ساعت از زمان اجرای تراکنش گذشته است.", "verify"),

  /** Success. */
  SUCCESS(0, "موفق", "verify | reverse"),

  /** Duplicate request. */
  DUPLICATE_REQUEST(2, "درخواست تکراری می باشد.", "verify | reverse"),

  /** Transaction already reversed. */
  ALREADY_REVERSED(5, "تراکنش برگشت خورده میباشد", "verify"),

  /** Terminal not found in system. */
  TERMINAL_NOT_FOUND(-105, "ترمینال ارسالی در سیستم موجود نمیباشد", "verify | reverse"),

  /** Terminal is inactive. */
  TERMINAL_INACTIVE(-104, "ترمینال ارسالی غیرفعال میباشد", "verify | reverse"),

  /** Request IP address is not allowed. */
  IP_NOT_ALLOWED(-106, "آدرس آی پی درخواستی غیرمجاز میباشد", "verify | reverse");

  private final int code;
  private final String description;
  private final String relatedApi;

  /**
   * Finds a result code by its integer value.
   *
   * @param code the result code
   * @return the matching SepResultCode, or null if not found
   */
  public static SepResultCode fromCode(int code) {
    for (SepResultCode rc : values()) {
      if (rc.code == code) {
        return rc;
      }
    }
    return null;
  }

  /** Whether this result code indicates success. */
  public boolean isSuccess() {
    return this == SUCCESS;
  }
}
