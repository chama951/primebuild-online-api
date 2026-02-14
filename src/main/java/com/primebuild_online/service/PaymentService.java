package com.primebuild_online.service;

import com.primebuild_online.model.DTO.PaymentDTO;
import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.Payment;

import java.util.Optional;

public interface PaymentService {
    Payment savePayment(Invoice invoice);

    Optional<Payment> getPaymentByInvoiceId(Long id);

    void updatePayment(Payment paymentInDb, Invoice finalInvoice);

    Payment updatePaymentReq(PaymentDTO paymentDTO, Long id);
}
