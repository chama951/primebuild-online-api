package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.InvoiceDTO;
import com.primebuild_online.model.Invoice;
import com.primebuild_online.service.InvoiceService;
import com.primebuild_online.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;
    private UserService userService;

    @PostMapping
    public ResponseEntity<Invoice> saveComponentFeatureTypeReq(@RequestBody InvoiceDTO invoiceDTO) {
        return new ResponseEntity<>(invoiceService.saveInvoice(invoiceDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Invoice> getAllInvoices(@RequestParam(value = "user_id", required = false) Long userId,
                                        @RequestParam(value = "date", required = false) String date,
                                        @RequestParam(value = "invoice_status", required = false) String invoiceStatus,
                                        @RequestParam(value = "user_type", required = false) String userType) {

        if (userId != null) {
            return invoiceService.getByUser(userId);
        }
        if (date != null) {
            return invoiceService.getByDate(date);
        }
        if (invoiceStatus != null) {
            return invoiceService.getByInvoiceStatus(invoiceStatus);
        }
        if (userType != null && Objects.equals(userType.toLowerCase(), "customer")) {
            return invoiceService.getByCustomerUser();
        }
        return invoiceService.getAllInvoices();
    }

    @PutMapping("{id}")
    private ResponseEntity<Invoice> updateInvoice(@PathVariable("id") Long id, @RequestBody InvoiceDTO inovoiceDTO) {
        return new ResponseEntity<Invoice>(invoiceService.updateInvoice(inovoiceDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable("id") Long id) {
        return new ResponseEntity<Invoice>(invoiceService.getInvoiceById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteInvoice(@PathVariable("id") Long id) {
        invoiceService.deleteInvoice(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invoice deleted Successfully");

        return ResponseEntity.ok(response);
    }

}
