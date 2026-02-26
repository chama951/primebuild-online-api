package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.*;
import com.primebuild_online.model.DTO.InvoiceDTO;
import com.primebuild_online.model.enumerations.InvoiceStatus;
import com.primebuild_online.model.enumerations.NotificationType;
import com.primebuild_online.repository.InvoiceRepository;
import com.primebuild_online.security.SecurityUtils;
import com.primebuild_online.service.*;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.InvoiceValidator;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceItemService invoiceItemService;
    private final InvoiceRepository invoiceRepository;
    private final UserService userService;
    private final PaymentService paymentService;
    private final InvoiceValidator invoiceValidator;
    private final NotificationService notificationService;

    public InvoiceServiceImpl(InvoiceItemService invoiceItemService,
                              InvoiceRepository invoiceRepository,
                              UserService userService,
                              @Lazy PaymentService paymentService,
                              InvoiceValidator invoiceValidator,
                              NotificationService notificationService) {
        this.invoiceItemService = invoiceItemService;
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
        this.paymentService = paymentService;
        this.invoiceValidator = invoiceValidator;
        this.notificationService = notificationService;
    }

    private User loggedInUser() {
        return userService.getUserById(
                Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId()
        );
    }

    @Override
    @Transactional
    public Invoice saveInvoice(InvoiceDTO invoiceDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Colombo"));
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate datePart = currentDateTime.toLocalDate();

        Invoice invoice = new Invoice();
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUser(loggedInUser());
        invoice.setInvoiceDate(datePart);

        if (invoiceDTO.getInvoiceStatus() != null) {
            invoice.setInvoiceStatus(InvoiceStatus.valueOf(invoiceDTO.getInvoiceStatus()));
        }

        invoice = invoiceRepository.save(invoice);
        invoice = invoiceRepository.save(
                createInvoiceItems(invoiceDTO.getItemList(), invoice));

        if (invoice.getInvoiceStatus().equals(InvoiceStatus.PAID)) {
            paymentService.savePayment(invoice);
        }

        invoiceValidator.validate(invoice);

        notificationService.createNotification(
                "New Invoice",
                "Invoice #" + invoice.getId() + " has been created successfully.",
                NotificationType.INVOICE_CREATED,
                loggedInUser()
        );

        return invoice;
    }

    @Override
    public List<Invoice> getByUser(Long userId) {
        return invoiceRepository.findAllByUser_UserId(userId);
    }

    public Invoice createInvoiceItems(List<Item> itemList, Invoice invoiceInDb) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        InvoiceItem invoiceItem;
        for (Item item : itemList) {
            invoiceItem = invoiceItemService.saveInvoiceItem(item, invoiceInDb);
            totalAmount = totalAmount.add(invoiceItem.getSubtotal());
            discountAmount = discountAmount.add(invoiceItem.getDiscountSubTotal());
            invoiceInDb.getInvoiceItems().add(invoiceItem);
        }
        invoiceInDb.setDiscountAmount(discountAmount);
        invoiceInDb.setTotalAmount(totalAmount);
        return invoiceInDb;
    }

    @Override
    public List<Invoice> getByDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate dateFormated = LocalDate.parse(date);

        return invoiceRepository.findAllByInvoiceDate(dateFormated);
    }

    @Override
    public List<Invoice> getByInvoiceStatus(String invoiceStatus) {
        return invoiceRepository.findAllByInvoiceStatus(InvoiceStatus.valueOf(invoiceStatus));
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice updateInvoice(InvoiceDTO invoiceDTO, Long id) {

        Invoice invoiceInDb = getInvoiceById(id);

        InvoiceStatus oldStatus = invoiceInDb.getInvoiceStatus();
        InvoiceStatus newStatus = InvoiceStatus.valueOf(invoiceDTO.getInvoiceStatus());

        invoiceInDb.setUpdatedAt(LocalDateTime.now());
        invoiceInDb.setUser(loggedInUser());
        invoiceInDb.setInvoiceStatus(InvoiceStatus.valueOf(invoiceDTO.getInvoiceStatus()));

        if (oldStatus.equals(InvoiceStatus.PAID)) {
            invoiceItemService.resetItemQuantity(invoiceInDb.getInvoiceItems());
        }

        invoiceInDb.getInvoiceItems().clear();

        invoiceInDb = invoiceRepository.save(invoiceInDb);

        invoiceInDb = invoiceRepository.save(
                createInvoiceItems(invoiceDTO.getItemList(), invoiceInDb));

        Invoice finalInvoice = invoiceInDb;
        Payment paymentInDb = paymentService.getPaymentByInvoiceId(invoiceInDb.getId())
                .orElseGet(() -> paymentService.savePayment(finalInvoice));

        paymentService.updatePendingPayment(paymentInDb, finalInvoice);

        if (newStatus.equals(InvoiceStatus.PAID)) {
            paymentService.updatePaidPayment(paymentInDb, finalInvoice);
        }

        invoiceValidator.validate(invoiceInDb);
        return invoiceInDb;
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isPresent()) {
            return invoice.get();
        } else {
            throw new PrimeBuildException(
                    "Invoice not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteInvoice(Long id) {
        if (paymentService.getAllPaymentsByInvoice(id)) {
            throw new PrimeBuildException(
                    "Invoice cannot be deleted while found in Payments",
                    HttpStatus.CONFLICT);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public List<Invoice> getByUserLoggedIn() {
        return invoiceRepository.findAllByUser(loggedInUser());
    }

    @Override
    public void updateNotPaidInvoice(Invoice invoice) {
        invoice.setInvoiceStatus(InvoiceStatus.NOT_PAID);
        invoiceItemService.resetItemQuantity(invoice.getInvoiceItems());
        invoiceRepository.save(invoice);
    }

}
