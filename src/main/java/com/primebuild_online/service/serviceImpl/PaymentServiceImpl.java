package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.PaymentDTO;
import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.Payment;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.PaymentStatus;
import com.primebuild_online.repository.PaymentRepository;
import com.primebuild_online.security.SecurityUtils;
import com.primebuild_online.service.InvoiceService;
import com.primebuild_online.service.PaymentService;
import com.primebuild_online.service.UserService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.PaymentValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
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
    private final PaymentValidator paymentValidator;
    private final InvoiceService invoiceService;

    public PaymentServiceImpl(UserService userService,
                              PaymentRepository paymentRepository,
                              PaymentValidator paymentValidator,
                              @Lazy InvoiceService invoiceService) {
        this.userService = userService;
        this.paymentRepository = paymentRepository;
        this.paymentValidator = paymentValidator;
        this.invoiceService = invoiceService;
    }

    private User loggedInUser() {
        return userService.getUserById(
                Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId()
        );
    }

    @Override
    public Payment savePaymentPending(Invoice invoice) {

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
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaidAt(LocalDateTime.now());

        paymentValidator.validate(payment);
        return paymentRepository.save(payment);
    }


    @Override
    public Optional<Payment> getPaymentByInvoiceId(Long id) {
        return paymentRepository.findByInvoice_Id(id);
    }

    @Override
    public void updatePaidPaymentAtInvoice(Payment paymentInDb, Invoice invoice) {
        paymentInDb.setInvoice(invoice);
        paymentInDb.setUser(loggedInUser());
        paymentInDb.setAmount(invoice.getTotalAmount());
        paymentInDb.setPaymentStatus(PaymentStatus.PAID);
        paymentInDb.setPaidAt(LocalDateTime.now());
        paymentValidator.validate(paymentInDb);
        paymentRepository.save(paymentInDb);
    }

    @Override
    public Payment updatePaymentReq(PaymentDTO paymentDTO, Long id) {
        Payment paymentInDb = getPaymentById(id);

        if (paymentInDb.getPaymentStatus().equals(PaymentStatus.PAID) && !paymentDTO.getPaymentStatus().equals(PaymentStatus.PAID)) {
            invoiceService.updateNotPaidInvoiceAtPayment(paymentInDb.getInvoice());
        }

        if (!paymentInDb.getPaymentStatus().equals(PaymentStatus.PAID) && paymentDTO.getPaymentStatus().equals(PaymentStatus.PAID)) {
            invoiceService.updatePaidInvoiceAtPayment(paymentInDb.getInvoice());
        }

        paymentInDb.setPaymentStatus(paymentDTO.getPaymentStatus());

        return paymentRepository.save(paymentInDb);
    }

    @Override
    public Payment getPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            return payment.get();
        } else {
            throw new PrimeBuildException(
                    "Payment not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Payment> getByUser(Long userId) {
        return paymentRepository.findAllByUser_UserId(userId);
    }

    @Override
    public List<Payment> getByDate(String date) {
        LocalDate dateFormated = LocalDate.parse(date);

        return paymentRepository.findAllByPaymentDate(dateFormated);
    }

    @Override
    public List<Payment> getByPaymentStatus(String paymentStatus) {
        return paymentRepository.findAllByPaymentStatus(PaymentStatus.valueOf(paymentStatus));
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getByUsername(String username) {
        return paymentRepository.findAllByUser_Username(username);
    }

    @Override
    public boolean getAllPaymentsByInvoice(Long id) {
        return paymentRepository.existsByInvoice_IdAndPaymentStatus(id, PaymentStatus.PAID);
    }

    @Override
    public void updateNotPaidPaymentAtInvoice(Payment paymentInDb, Invoice finalInvoice) {
        paymentInDb.setInvoice(finalInvoice);
        paymentInDb.setUser(loggedInUser());
        paymentInDb.setAmount(finalInvoice.getTotalAmount());
        paymentInDb.setPaymentStatus(PaymentStatus.PENDING);
        paymentInDb.setPaidAt(LocalDateTime.now());
        paymentValidator.validate(paymentInDb);
        paymentRepository.save(paymentInDb);
    }

}
