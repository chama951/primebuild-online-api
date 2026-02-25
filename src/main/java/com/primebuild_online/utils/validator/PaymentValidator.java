package com.primebuild_online.utils.validator;

import com.primebuild_online.model.Payment;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentValidator {
    public void validate(Payment payment) {

        if (payment == null) {
            throw new PrimeBuildException(
                    "Payment object must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (payment.getInvoice() == null) {
            throw new PrimeBuildException(
                    "Payment must be linked to an invoice",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (payment.getUser() == null) {
            throw new PrimeBuildException(
                    "Payment must be linked to a user",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (payment.getAmount() == null ||
                payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new PrimeBuildException(
                    "Payment amount must be greater than 0",
                    HttpStatus.BAD_REQUEST
            );
        }

//        if (payment.getPaymentMethods() == null) {
//            throw new PrimeBuildException(
//                    "Payment method must not be null",
//                    HttpStatus.BAD_REQUEST
//            );
//        }

        if (payment.getPaymentStatus() == null) {
            throw new PrimeBuildException(
                    "Payment status must not be null",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
