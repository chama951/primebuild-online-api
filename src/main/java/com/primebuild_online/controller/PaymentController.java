package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.PaymentDTO;
import com.primebuild_online.model.Payment;
import com.primebuild_online.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PutMapping("{id}")
    private ResponseEntity<Payment> updatePayment(@PathVariable("id") Long id, @RequestBody PaymentDTO paymentDTO) {
        return new ResponseEntity<Payment>(paymentService.updatePaymentReq(paymentDTO, id), HttpStatus.OK);
    }
}
