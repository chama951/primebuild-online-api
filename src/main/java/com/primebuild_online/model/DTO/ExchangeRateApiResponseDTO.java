package com.primebuild_online.model.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRateApiResponseDTO {
    private String result;
    private Map<String, Double> rates;
}