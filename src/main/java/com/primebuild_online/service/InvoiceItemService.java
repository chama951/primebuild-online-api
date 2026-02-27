package com.primebuild_online.service;

import com.primebuild_online.model.CartItem;
import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.InvoiceItem;
import com.primebuild_online.model.Item;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceItemService {
    InvoiceItem saveInvoiceItem(Item item, Invoice invoice);

    void deleteInvoiceItemsByInvoiceId(Long id);

    void resetItemQuantity(List<InvoiceItem> invoiceItems);

    boolean existsInvoiceByItem(Long id);

    BigDecimal calculateDiscountAmount(List<InvoiceItem> invoiceItems);

    BigDecimal calculateTotalAmount(List<InvoiceItem> invoiceItems);

    void updateInvoiceItemAtPriceChange(List<InvoiceItem> invoiceItems);
}
