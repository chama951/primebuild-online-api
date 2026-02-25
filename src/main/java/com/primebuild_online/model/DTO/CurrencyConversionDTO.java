package com.primebuild_online.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionDTO {
    private BigDecimal usdAmount;
    private BigDecimal lkrAmount;
}
