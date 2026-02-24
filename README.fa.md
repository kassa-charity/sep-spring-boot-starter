# SEP Spring Boot Starter

استارتر اسپرینگ بوت برای اتصال به **درگاه پرداخت الکترونیک سامان کیش (SEP / شاپرک)**. با این کتابخانه می‌توانید درخواست توکن پرداخت، هدایت کاربر به درگاه، و تأیید یا برگشت تراکنش را انجام دهید.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

## امکانات

- **درخواست توکن** — دریافت توکن پرداخت برای یک تراکنش (مبلغ، شماره پیگیری، آدرس بازگشت).
- **آدرس بازگشت** — ساخت آدرس هدایت به درگاه با استفاده از توکن.
- **تأیید تراکنش** — تأیید پرداخت با استفاده از شماره مرجع (RefNum) برگشتی از درگاه.
- **برگشت تراکنش** — برگشت (refund) تراکنش با شماره مرجع.
- پیکربندی خودکار با `SepProperties` و bean آماده‌ی `SepClient`.

## پیش‌نیازها

- جاوا ۱۷ یا بالاتر
- اسپرینگ بوت ۴.x

## نصب

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

## پیکربندی

تنظیمات درگاه SEP را در `application.yml` یا `application.properties` قرار دهید:

```yaml
sep:
  base-url: https://sep.shaparak.ir   # اختیاری؛ مقدار پیش‌فرض همین است
  terminal-id: "شماره_ترمینال_شما"   # الزامی
```

```properties
sep.base-url=https://sep.shaparak.ir
sep.terminal-id=شماره_ترمینال_شما
```

- **base-url** — آدرس پایه درگاه SEP (پیش‌فرض: `https://sep.shaparak.ir`).
- **terminal-id** — شماره ترمینال دریافتی از درگاه پرداخت.

## نحوه استفاده

کلاس `SepClient` را تزریق کنید و در فلوی پرداخت از آن استفاده کنید.

### ۱. درخواست توکن و هدایت کاربر به درگاه

```java
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final SepClient sepClient;

    @PostMapping("/payment/init")
    public String initPayment(@RequestBody PaymentRequest request) {
        long amountRials = request.getAmount();           // مبلغ به ریال
        String resNum = "ORDER-" + UUID.randomUUID();     // شماره پیگیری یکتا
        String redirectUrl = "https://yourapp.com/payment/callback";
        String cellNumber = request.getMobile();           // اختیاری؛ می‌توان null باشد

        SepTokenResponse response = sepClient.requestToken(
            amountRials, resNum, redirectUrl, cellNumber
        );
        String token = response.getToken();

        String redirectToGateway = sepClient.getRedirectUrl(token);
        return redirectToGateway;   // کاربر را به این آدرس هدایت کنید
    }
}
```

### ۲. دریافت بازگشت از درگاه و تأیید تراکنش

پس از پرداخت کاربر، درگاه به آدرس `redirectUrl` شما با پارامتر `RefNum` (و سایر پارامترها) برمی‌گردد. تراکنش را تأیید کنید:

```java
@GetMapping("/payment/callback")
public String callback(@RequestParam(required = false) String RefNum,
                      @RequestParam(required = false) String ResNum) {
    if (RefNum == null || RefNum.isBlank()) {
        return "redirect:/payment/failed";
    }
    try {
        SepVerifyTransactionResponse verify = sepClient.verifyTransaction(RefNum);
        // از verify.getTransactionDetail() برای مبلغ، شماره کارت و غیره استفاده کنید
        return "redirect:/payment/success?ref=" + RefNum;
    } catch (SepGatewayException e) {
        // e.getErrorCode() ، e.getMessage()
        return "redirect:/payment/failed";
    }
}
```

### ۳. برگشت تراکنش (refund)

```java
SepVerifyTransactionResponse reverse = sepClient.reverseTransaction(refNum);
```

## خلاصه API

| متد | توضیح |
|-----|--------|
| `requestToken(amount, resNum, redirectUrl, cellNumber)` | درخواست توکن پرداخت؛ در صورت خطای درگاه `SepGatewayException` پرتاب می‌شود. |
| `getRedirectUrl(token)` | آدرس کامل برای هدایت کاربر به درگاه. |
| `verifyTransaction(refNum)` | تأیید تراکنش با شماره مرجع. |
| `reverseTransaction(refNum)` | برگشت تراکنش با شماره مرجع. |

خطاهای درگاه به صورت `SepGatewayException` (شامل کد و توضیح) پرتاب می‌شوند.

## کدهای نتیجه (Verify / Reverse)

| کد | توضیح | API |
|----|--------|-----|
| ۰ | موفق | verify \| reverse |
| ۲ | درخواست تکراری | verify \| reverse |
| ۵ | تراکنش قبلاً برگشت خورده | verify |
| -۲ | تراکنش یافت نشد | verify |
| -۶ | بیش از نیم ساعت از تراکنش گذشته | verify |
| -۱۰۴ | ترمینال غیرفعال | verify \| reverse |
| -۱۰۵ | ترمینال در سیستم موجود نیست | verify \| reverse |
| -۱۰۶ | آدرس IP غیرمجاز | verify \| reverse |

از enum های `SepErrorCode` (برای درخواست توکن) و `SepResultCode` (برای verify/reverse) می‌توانید برای تفسیر کدها استفاده کنید.

## مجوز

این پروژه تحت [مجوز MIT](LICENSE) منتشر شده است.

برای انتشار در Maven Central (Sonatype) به [docs/PUBLISHING.md](docs/PUBLISHING.md) مراجعه کنید.
