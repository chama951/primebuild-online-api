package com.primebuild_online.service;

import com.primebuild_online.model.DTO.InvoiceDTO;
import com.primebuild_online.model.Invoice;
import com.primebuild_online.model.Item;

import java.util.List;

public interface InvoiceService {
    Invoice saveInvoice(InvoiceDTO invoiceDTO);

    List<Invoice> getByUser(Long userId);

    List<Invoice> getByDate(String date);

    List<Invoice> getByInvoiceStatus(String invoiceStatus);

    List<Invoice> getAllInvoices();

    Invoice updateInvoice(InvoiceDTO invoiceDTO, Long id);

    Invoice getInvoiceById(Long id);

    void deleteInvoice(Long id);

    void updateInvoiceAtItemPriceChange(Item item);

    List<Invoice> getByUserLoggedIn();

    void updateNotPaidInvoice(Invoice invoice);
}
