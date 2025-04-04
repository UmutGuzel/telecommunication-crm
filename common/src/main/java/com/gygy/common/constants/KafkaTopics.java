package com.gygy.common.constants;

public final class KafkaTopics {

    // Tüm topic isimleri tek bir yerde toplanıyor, bir topic ismini değiştirmemiz gerekirse, sadece burada değiştiriyoruz
    // Yazım Hatası Önlüyor:
    //   (string yazım hatası olabilir)
    //   kafkaTemplate.send("payment-success-topic", event);
    //
    //   // Doğru kullanım (IDE otomatik tamamlama yapar, yazım hatası olmaz)
    //   kafkaTemplate.send(KafkaTopics.PAYMENT_SUCCESS, event);

    private KafkaTopics() {
        throw new IllegalStateException("Constant class");
    }

    // Payment Events
    public static final String PAYMENT_SUCCESS = "payment-success-topic";
    public static final String PAYMENT_FAILED = "payment-failed-topic";

    // Bill Events
    public static final String BILL_CREATED = "bill-created-topic";
    public static final String BILL_PAID = "bill-paid-topic";
    public static final String BILL_OVERDUE = "bill-overdue-topic";
}