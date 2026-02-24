# SEP Spring Boot Starter

Spring Boot starter for **پرداخت الکترونیک سامان کیش** (SEP) — Shaparak Electronic Payment gateway integration. Use it to request payment tokens, redirect users to the gateway, and verify or reverse transactions.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

## Features

- **Token request** — Get a payment token for a transaction (amount, reference number, redirect URL).
- **Redirect URL** — Build the gateway redirect URL from the token.
- **Verify transaction** — Verify a payment using the reference number returned by the gateway.
- **Reverse transaction** — Reverse (refund) a transaction by reference number.
- Auto-configuration via `SepProperties` and a ready-to-use `SepClient` bean.

## Requirements

- Java 17+
- Spring Boot 4.x

## Installation

### Maven

```xml
<dependency>
    <groupId>io.github.kassa-charity</groupId>
    <artifactId>sep-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle (Groovy)

```groovy
implementation 'io.github.kassa-charity:sep-spring-boot-starter:1.0.0'
```

### Gradle (Kotlin)

```kotlin
implementation("io.github.kassa-charity:sep-spring-boot-starter:1.0.0")
```

## Configuration

Configure the SEP gateway in `application.yml` or `application.properties`:

```yaml
sep:
  base-url: https://sep.shaparak.ir   # optional; this is the default
  terminal-id: "YOUR_TERMINAL_ID"     # required
```

```properties
sep.base-url=https://sep.shaparak.ir
sep.terminal-id=YOUR_TERMINAL_ID
```

- **base-url** — SEP gateway base URL (default: `https://sep.shaparak.ir`).
- **terminal-id** — Your terminal ID from the payment gateway.

## Usage

Inject `SepClient` and use it in your payment flow.

### 1. Request a token and redirect the user

```java
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final SepClient sepClient;

    @PostMapping("/payment/init")
    public String initPayment(@RequestBody PaymentRequest request) {
        long amountRials = request.getAmount();
        String resNum = "ORDER-" + UUID.randomUUID();  // unique reference
        String redirectUrl = "https://yourapp.com/payment/callback";
        String cellNumber = request.getMobile();        // optional, can be null

        SepTokenResponse response = sepClient.requestToken(
            amountRials, resNum, redirectUrl, cellNumber
        );
        String token = response.getToken();

        String redirectToGateway = sepClient.getRedirectUrl(token);
        return redirectToGateway;  // redirect user to this URL
    }
}
```

### 2. Handle callback and verify the transaction

After the user pays, the gateway redirects to your `redirectUrl` with a `RefNum` (and other params). Verify the transaction:

```java
@GetMapping("/payment/callback")
public String callback(@RequestParam(required = false) String RefNum,
                      @RequestParam(required = false) String ResNum) {
    if (RefNum == null || RefNum.isBlank()) {
        return "redirect:/payment/failed";
    }
    try {
        SepVerifyTransactionResponse verify = sepClient.verifyTransaction(RefNum);
        // use verify.getTransactionDetail() for amount, card number, etc.
        return "redirect:/payment/success?ref=" + RefNum;
    } catch (SepGatewayException e) {
        // e.getErrorCode(), e.getMessage()
        return "redirect:/payment/failed";
    }
}
```

### 3. Reverse (refund) a transaction

```java
SepVerifyTransactionResponse reverse = sepClient.reverseTransaction(refNum);
```

## API summary

| Method | Description |
|--------|-------------|
| `requestToken(amount, resNum, redirectUrl, cellNumber)` | Request payment token; throws `SepGatewayException` on gateway error. |
| `getRedirectUrl(token)` | Returns the full URL to redirect the user to the gateway. |
| `verifyTransaction(refNum)` | Verify a transaction by reference number. |
| `reverseTransaction(refNum)` | Reverse (refund) a transaction by reference number. |

Errors from the gateway are thrown as `SepGatewayException` (code and message).

## License

This project is licensed under the [MIT License](LICENSE).

For publishing to Maven Central (Sonatype), see [docs/PUBLISHING.md](docs/PUBLISHING.md).
