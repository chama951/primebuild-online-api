package com.primebuild_online.repository;

import com.primebuild_online.model.Payment;
import com.primebuild_online.model.enumerations.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByInvoice_Id(Long id);

    List<Payment> findAllByUser_UserId(Long userId);

    List<Payment> findAllByCreatedAt(LocalDate dateFormated);

    List<Payment> findAllByPaymentStatus(PaymentStatus paymentStatus);
}
