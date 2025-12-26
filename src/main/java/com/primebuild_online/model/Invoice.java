package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
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

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @Column(name = "pc_build_id", nullable = false)
    private Long pcBuildId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pc_build_id", insertable = false, updatable = false)
    private Build build;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems;

}
