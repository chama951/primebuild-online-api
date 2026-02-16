package com.primebuild_online.model.DTO;

import com.primebuild_online.model.enumerations.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private PaymentStatus paymentStatus;
}
