package com.gygy.common.constants;

public final class KafkaTopics {

    // Tüm topic isimleri tek bir yerde toplanıyor, bir topic ismini değiştirmemiz
    // gerekirse, sadece burada değiştiriyoruz
    // Yazım Hatası Önlüyor:
    // (string yazım hatası olabilir)
    // kafkaTemplate.send("payment-success-topic", event);
    //
    // // Doğru kullanım (IDE otomatik tamamlama yapar, yazım hatası olmaz)
    // kafkaTemplate.send(KafkaTopics.PAYMENT_SUCCESS, event);

    private KafkaTopics() {
        throw new IllegalStateException("Constant class");
    }

    // Payment Events
    public static final String PAYMENT_SUCCESS = "payment-success-topic";
    public static final String PAYMENT_FAILED = "payment-failed-topic";
    public static final String PAYMENT_CREATED = "payment-created-topic";
    public static final String PAYMENT_STATUS_UPDATED = "payment-status-updated-topic";
    public static final String PAYMENT_DELETED = "payment-deleted-topic";

    // Bill Events
    public static final String BILL_CREATED = "bill-created-topic";
    public static final String BILL_PAID = "bill-paid-topic";
    public static final String BILL_OVERDUE = "bill-overdue-topic";
    public static final String BILL_DELETED = "bill-deleted-topic";
    public static final String BILL_STATUS_UPDATED = "bill-status-updated-topic";
    public static final String BILL_AMOUNT_UPDATED = "bill-amount-updated-topic";

    // Customer Events
    public static final String INDIVIDUAL_CUSTOMER_CREATED = "individual-customer-created-topic";
    public static final String CORPORATE_CUSTOMER_CREATED = "corporate-customer-created-topic";
    public static final String INDIVIDUAL_CUSTOMER_UPDATED = "individual-customer-updated-topic";
    public static final String CORPORATE_CUSTOMER_UPDATED = "corporate-customer-updated-topic";
}