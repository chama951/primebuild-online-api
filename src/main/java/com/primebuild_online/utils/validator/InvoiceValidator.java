package com.primebuild_online.utils.validator;

import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.InvoiceItem;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class InvoiceValidator {
    public void validate(Invoice invoice) {

        if (invoice == null) {
            throw new PrimeBuildException(
                    "Invoice object must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (invoice.getInvoiceStatus() == null) {
            throw new PrimeBuildException(
                    "Invoice status must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (invoice.getInvoiceItems() == null || invoice.getInvoiceItems().isEmpty()) {
            throw new PrimeBuildException(
                    "Invoice must contain at least one item",
                    HttpStatus.BAD_REQUEST
            );
        }

        for (int i = 0; i < invoice.getInvoiceItems().size(); i++) {
            InvoiceItem item = invoice.getInvoiceItems().get(i);

            if (item.getItem() == null) {
                throw new PrimeBuildException(
                        "Invoice item reference at index " + i + " must not be null",
                        HttpStatus.BAD_REQUEST
                );
            }

            if (item.getInvoiceQuantity() == null || item.getInvoiceQuantity() < 1) {
                throw new PrimeBuildException(
                        "Invoice item quantity at index " + i + " must be at least 1",
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        if (invoice.getDiscountAmount() != null &&
                invoice.getTotalAmount() != null &&
                invoice.getDiscountAmount().compareTo(invoice.getTotalAmount()) > 0) {

            throw new PrimeBuildException(
                    "Discount amount cannot exceed total amount",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
