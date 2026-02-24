package com.kassa.sep;

import org.springframework.web.client.RestClient;

import com.kassa.sep.dto.SepTokenRequest;
import com.kassa.sep.dto.SepTokenResponse;
import com.kassa.sep.dto.SepVerifyTransactionRequest;
import com.kassa.sep.dto.SepVerifyTransactionResponse;
import com.kassa.sep.exception.SepGatewayException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SepClient {
  private static final String TOKEN_PATH = "/onlinepg/onlinepg";
  private static final String VERIFY_PATH = "/verifyTxnRandomSessionkey/ipg/VerifyTransaction";
  private static final String REVERSE_PATH = "/verifyTxnRandomSessionkey/ipg/ReverseTransaction";
  private static final String ACTION_TOKEN = "token";

  private final RestClient restClient;
  private final SepProperties properties;

  public SepClient(RestClient restClient, SepProperties properties) {
    this.restClient = restClient;
    this.properties = properties;
  }

  /**
   * Requests a payment token from the SEP gateway for the given transaction.
   * The token is used to redirect the user to the gateway payment page.
   * <p>
   * On success (status = 1), returns a response containing the token.
   * On error (status != 1, typically -1), throws {@link SepGatewayException} with error details.
   *
   * @param amount      transaction amount in Rials
   * @param resNum      merchant reservation/reference number (unique per transaction)
   * @param redirectUrl URL to which the user is redirected after payment
   * @param cellNumber  optional payer mobile number (e.g. "9120000000"), may be null
   * @return the gateway response containing status and token (only on success)
   * @throws SepGatewayException                              if the gateway returns an error response
   * @throws org.springframework.web.client.RestClientException if the HTTP request fails
   */
  public SepTokenResponse requestToken(long amount, String resNum, String redirectUrl, String cellNumber) {
    var request = SepTokenRequest.builder()
      .action(ACTION_TOKEN)
      .terminalId(properties.terminalId())
      .amount(amount)
      .resNum(resNum)
      .redirectUrl(redirectUrl)
      .cellNumber(cellNumber)
      .build();

    SepTokenResponse response = restClient.post()
      .uri(TOKEN_PATH)
      .body(request)
      .retrieve()
      .body(SepTokenResponse.class);

    if (response == null) {
      throw new SepGatewayException("UNKNOWN", "No response received from gateway");
    }

    if (!response.isSuccess()) {
      log.error("SEP Gateway error: code={}, desc={}", response.getErrorCode(), response.getErrorDesc());
      throw new SepGatewayException(response.getErrorCode(), response.getErrorDesc());
    }

    return response;
  }

  public String getRedirectUrl(String token) {
    return String.format("%s/OnlinePG/SendToken?token=%s", properties.baseUrl(), token);
  }
  
  /**
   * Verifies a transaction with the SEP gateway using the reference number
   * returned after the user completes payment.
   * <p>
   * On success (Success = true, ResultCode = 0), returns the response including
   * transaction detail. On error, throws {@link SepGatewayException}.
   *
   * @param refNum reference number (RefNum) from the gateway callback
   * @return the verify transaction response including transaction detail on success
   * @throws SepGatewayException                              if verification fails (Success = false or ResultCode != 0)
   * @throws org.springframework.web.client.RestClientException if the HTTP request fails
   */
  public SepVerifyTransactionResponse verifyTransaction(String refNum) {
    int terminalNumber = Integer.parseInt(properties.terminalId());
    var request = SepVerifyTransactionRequest.builder()
      .refNum(refNum)
      .terminalNumber(terminalNumber)
      .build();

    SepVerifyTransactionResponse response = restClient.post()
      .uri(VERIFY_PATH)
      .body(request)
      .retrieve()
      .body(SepVerifyTransactionResponse.class);

    if (response == null) {
      throw new SepGatewayException("UNKNOWN", "No response received from gateway");
    }

    if (!response.isSuccess()) {
      String code = response.getResultCode() != null ? String.valueOf(response.getResultCode()) : "UNKNOWN";
      String desc = response.getResultDescription() != null ? response.getResultDescription() : "Verification failed";
      log.error("SEP Verify error: code={}, desc={}", code, desc);
      throw new SepGatewayException(code, desc);
    }

    return response;
  }

  /**
   * Reverses (refunds) a transaction with the SEP gateway using the reference number.
   * <p>
   * On success (Success = true, ResultCode = 0), returns the response including
   * transaction detail. On error, throws {@link SepGatewayException}.
   * Result codes are documented in {@link com.kassa.sep.dto.SepResultCode}.
   *
   * @param refNum reference number (RefNum) from the gateway
   * @return the response including transaction detail on success
   * @throws SepGatewayException                              if reverse fails (Success = false or ResultCode != 0)
   * @throws org.springframework.web.client.RestClientException if the HTTP request fails
   */
  public SepVerifyTransactionResponse reverseTransaction(String refNum) {
    int terminalNumber = Integer.parseInt(properties.terminalId());
    var request = SepVerifyTransactionRequest.builder()
      .refNum(refNum)
      .terminalNumber(terminalNumber)
      .build();

    SepVerifyTransactionResponse response = restClient.post()
      .uri(REVERSE_PATH)
      .body(request)
      .retrieve()
      .body(SepVerifyTransactionResponse.class);

    if (response == null) {
      throw new SepGatewayException("UNKNOWN", "No response received from gateway");
    }

    if (!response.isSuccess()) {
      String code = response.getResultCode() != null ? String.valueOf(response.getResultCode()) : "UNKNOWN";
      String desc = response.getResultDescription() != null ? response.getResultDescription() : "Reverse failed";
      log.error("SEP Reverse error: code={}, desc={}", code, desc);
      throw new SepGatewayException(code, desc);
    }

    return response;
  }
}
