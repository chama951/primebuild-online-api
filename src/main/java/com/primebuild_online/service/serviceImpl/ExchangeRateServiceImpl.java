package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.CurrencyConversionDTO;
import com.primebuild_online.model.DTO.ExchangeRateApiResponseDTO;
import com.primebuild_online.model.ExchangeRate;
import com.primebuild_online.model.enumerations.Currency;
import com.primebuild_online.repository.ExchangeRateRepository;
import com.primebuild_online.service.ExchangeRateService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;

    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository,
                                   RestTemplate restTemplate) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.restTemplate = restTemplate;
    }

    private static final String API_URL =
            "https://open.er-api.com/v6/latest/USD";

    @Override
    public ExchangeRate fetchAndSaveUsdToLkrRate() {

        LocalDate today = LocalDate.now();
        ExchangeRate existingDailyRate = exchangeRateRepository
                .findByFromCurrencyAndToCurrencyAndDate(Currency.USD, Currency.LKR, today);

        if (existingDailyRate != null) {
            return existingDailyRate;
        }

        ExchangeRateApiResponseDTO response =
                restTemplate.getForObject(API_URL, ExchangeRateApiResponseDTO.class);

        if (response == null || response.getRates() == null
                || !"success".equalsIgnoreCase(response.getResult())) {
            throw new PrimeBuildException(
                    "Failed to fetch exchange rate from API",
                    HttpStatus.NOT_FOUND);
        }

        Double lkrRate = response.getRates().get("LKR");
        if (lkrRate == null) {
            throw new PrimeBuildException(
                    "LKR rate not found in API response",
                    HttpStatus.NOT_FOUND);
        }

        ExchangeRate exchangeRate = new ExchangeRate();

        exchangeRate.setFromCurrency(Currency.USD);
        exchangeRate.setToCurrency(Currency.LKR);
        exchangeRate.setRate(lkrRate);
        exchangeRate.setLastUpdated(LocalDateTime.now());
        exchangeRate.setDate(today);

        return exchangeRateRepository.save(exchangeRate);
    }

    public Double getUsdToLkrRate() {
        return exchangeRateRepository
                .findByFromCurrencyAndToCurrencyAndDate(Currency.USD, Currency.LKR, LocalDate.now()).getRate();
    }

    @Override
    public CurrencyConversionDTO convertUsdToLkr(CurrencyConversionDTO currencyConversionDTO) {
        BigDecimal rate = BigDecimal.valueOf(getUsdToLkrRate());

        BigDecimal lkrAmount = currencyConversionDTO.getUsdAmount().multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);

        currencyConversionDTO.setLkrAmount(lkrAmount);
        return currencyConversionDTO;
    }

}
