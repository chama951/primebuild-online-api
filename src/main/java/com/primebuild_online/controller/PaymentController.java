package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.PaymentDTO;
import com.primebuild_online.model.Payment;
import com.primebuild_online.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PutMapping("{id}")
    private ResponseEntity<Payment> updatePayment(@PathVariable("id") Long id, @RequestBody PaymentDTO paymentDTO) {
        return new ResponseEntity<Payment>(paymentService.updatePaymentReq(paymentDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") Long id) {
        return new ResponseEntity<Payment>(paymentService.getPaymentById(id), HttpStatus.OK);
    }

    @GetMapping
    public List<Payment> getAllPayment(@RequestParam(value = "user_id", required = false) Long userId,
                                       @RequestParam(value = "date", required = false) String date,
                                       @RequestParam(value = "payment_status", required = false) String paymentStatus,
                                       @RequestParam(value = "username", required = false) String username) {

        if (userId != null) {
            return paymentService.getByUser(userId);
        }
        if (date != null) {
            return paymentService.getByDate(date);
        }
        if (paymentStatus != null) {
            return paymentService.getByPaymentStatus(paymentStatus);
        }
        if (paymentStatus != null) {
            return paymentService.getByPaymentStatus(paymentStatus);
        }
        if (username != null) {
            return paymentService.getByUsername(username);
        }

        return paymentService.getAllPayments();
    }

}
