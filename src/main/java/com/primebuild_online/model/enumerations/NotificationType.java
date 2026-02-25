package com.primebuild_online.model.enumerations;

public enum NotificationType {
    // Customer
    INVOICE_CREATED,
    CART_ITEM_PRICE_REDUCED,
    BUILD_ITEM_PRICE_REDUCED,

    // Admin
    LOW_STOCK,
    EXCHANGE_RATE,

    // Both
    TRENDING_ITEM
}
