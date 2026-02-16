package com.primebuild_online.repository;

import com.primebuild_online.model.InvoiceItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
    @Transactional
    @Modifying
    void deleteAllByInvoice_Id(Long id);
}
