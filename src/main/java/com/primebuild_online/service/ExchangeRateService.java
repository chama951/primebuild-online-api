package com.primebuild_online.service;

import com.primebuild_online.model.DTO.CurrencyConversionDTO;
import com.primebuild_online.model.ExchangeRate;

public interface ExchangeRateService {
    ExchangeRate fetchAndSaveUsdToLkrRate();
    CurrencyConversionDTO convertUsdToLkr(CurrencyConversionDTO currencyConversionDTO);
}
