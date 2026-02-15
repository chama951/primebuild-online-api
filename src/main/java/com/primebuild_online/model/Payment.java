package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.primebuild_online.model.enumerations.PaymentMethods;
import com.primebuild_online.model.enumerations.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "invoice"})
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
    private User user;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency = "LKR";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethods paymentMethods;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}