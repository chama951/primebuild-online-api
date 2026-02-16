package com.primebuild_online.service;

import com.primebuild_online.model.DTO.PaymentDTO;
import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment savePayment(Invoice invoice);

    Optional<Payment> getPaymentByInvoiceId(Long id);

    void updatePayment(Payment paymentInDb, Invoice finalInvoice);

    Payment updatePaymentReq(PaymentDTO paymentDTO, Long id);

    Payment getPaymentById(Long id);

    List<Payment> getByUser(Long userId);

    List<Payment> getByDate(String date);

    List<Payment> getByPaymentStatus(String paymentStatus);

    List<Payment> getAllPayments();
}
