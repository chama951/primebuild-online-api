package com.primebuild_online.service;

import com.primebuild_online.model.DTO.CurrencyConversionDTO;
import com.primebuild_online.model.ExchangeRate;

import java.util.List;

public interface ExchangeRateService {
    ExchangeRate fetchAndSaveUsdToLkrRate();

    CurrencyConversionDTO convertUsdToLkr(CurrencyConversionDTO currencyConversionDTO);

    List<ExchangeRate> getExchangeRateListDaysBefore(Long days);
}
