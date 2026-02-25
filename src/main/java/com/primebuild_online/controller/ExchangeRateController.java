package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.CurrencyConversionDTO;
import com.primebuild_online.model.ExchangeRate;
import com.primebuild_online.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange_rate")
public class ExchangeRateController {

    @Autowired
    ExchangeRateService exchangeRateService;

    @PostMapping
    public CurrencyConversionDTO convertUsdToLkr(@RequestBody CurrencyConversionDTO currencyConversionDTO) {
        return exchangeRateService.convertUsdToLkr(currencyConversionDTO);
    }

    @GetMapping
    public ExchangeRate getExchangeRate() {
        return exchangeRateService.fetchAndSaveUsdToLkrRate();
    }

    @GetMapping("/{days}")
    public List<ExchangeRate> getExchangeRateListDaysBefore(@PathVariable("days") Long days) {
        return exchangeRateService.getExchangeRateListDaysBefore(days);
    }
}
