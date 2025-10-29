package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "status", nullable = false)
    private String status; // "DRAFT", "SENT", "PAID", "OVERDUE", "CANCELLED", "REFUNDED"

    @Column(name = "payment_method")
    private String paymentMethod; // "CREDIT_CARD", "PAYPAL", "BANK_TRANSFER", "CASH"

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "billing_address")
    private String billingAddress;

    // Relationships
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @Column(name = "pc_build_id", nullable = false)
    private Long pcBuildId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pc_build_id", insertable = false, updatable = false)
    private PcBuild pcBuild;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems;

    // Constructors
    public Invoice() {
        this.invoiceDate = LocalDateTime.now();
        this.status = "DRAFT";
        this.subtotal = 0.0;
        this.discountAmount = 0.0;
        this.totalAmount = 0.0;
    }

    public Invoice(Long customerId, Long pcBuildId) {
        this();
        this.customerId = customerId;
        this.pcBuildId = pcBuildId;
//        this.invoiceNumber = generateInvoiceNumber();
    }
}
