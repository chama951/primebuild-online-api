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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Colombo"));
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate datePart = currentDateTime.toLocalDate();

        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setPaymentDate(datePart);
        payment.setInvoice(invoice);
        payment.setUser(loggedInUser());
        payment.setAmount(invoice.getTotalAmount());
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
        paymentInDb.setPaymentStatus(PaymentStatus.PAID);
        paymentInDb.setPaidAt(LocalDateTime.now());
        paymentRepository.save(paymentInDb);
    }

    @Override
    public Payment updatePaymentReq(PaymentDTO paymentDTO, Long id) {
        Payment paymentInDb = getPaymentById(id);
        paymentInDb.setPaymentStatus(paymentDTO.getPaymentStatus());
        return paymentRepository.save(paymentInDb);
    }

    @Override
    public Payment getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            return payment.get();
        } else {
            throw new RuntimeException("Payment not found");
        }
    }

    @Override
    public List<Payment> getByUser(Long userId) {
        return paymentRepository.findAllByUser_UserId(userId);
    }

    @Override
    public List<Payment> getByDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate dateFormated = LocalDate.parse(date);

        return paymentRepository.findAllByCreatedAt(dateFormated);
    }

    @Override
    public List<Payment> getByPaymentStatus(String paymentStatus) {
        return paymentRepository.findAllByPaymentStatus(PaymentStatus.valueOf(paymentStatus));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
