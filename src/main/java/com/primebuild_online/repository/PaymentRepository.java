package com.primebuild_online.repository;

import com.primebuild_online.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByInvoice_Id(Long id);
}
