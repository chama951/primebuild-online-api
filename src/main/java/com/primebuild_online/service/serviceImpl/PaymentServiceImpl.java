package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.PaymentDTO;
import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.Payment;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.PaymentStatus;
import com.primebuild_online.repository.PaymentRepository;
import com.primebuild_online.security.SecurityUtils;
import com.primebuild_online.service.InvoiceService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.service.PaymentService;
import com.primebuild_online.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final UserService userService;
    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;

    public PaymentServiceImpl(UserService userService, PaymentRepository paymentRepository, ItemService itemService, InvoiceService invoiceService) {
        this.userService = userService;
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
    }

    private User loggedInUser() {
        return userService.getUserById(
                Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId()
        );
    }

    @Override
    public Payment savePayment(Invoice invoice) {
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setInvoice(invoice);
        payment.setUser(loggedInUser());
        payment.setAmount(invoice.getTotalAmount());
//        payment.setPaymentMethods(PaymentMethods.CASH);
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }


    @Override
    public Optional<Payment> getPaymentByInvoiceId(Long id) {
        return paymentRepository.findByInvoice_Id(id);
    }

    @Override
    public void updatePayment(Payment paymentInDb, Invoice invoice) {
        paymentInDb.setInvoice(invoice);
        paymentInDb.setUser(loggedInUser());
        paymentInDb.setAmount(invoice.getTotalAmount());
//        payment.setPaymentMethods(PaymentMethods.CASH);
        paymentInDb.setPaymentStatus(PaymentStatus.PAID);
        paymentInDb.setPaidAt(LocalDateTime.now());
        paymentRepository.save(paymentInDb);
    }

    @Override
    public Payment updatePaymentReq(PaymentDTO paymentDTO, Long id) {
        Optional<Payment> paymentInDb = paymentRepository.findByInvoice_Id(id);
        Payment payment = paymentInDb.get();
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        if (payment.getPaymentStatus().equals(PaymentStatus.PENDING) ||
                payment.getPaymentStatus().equals(PaymentStatus.CANCELLED)) {
            invoiceService.updateInvoiceByPaymentStatus(payment.getInvoice());
        }
        paymentRepository.save(payment);
        return payment;
    }
}
