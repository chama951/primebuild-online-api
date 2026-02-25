package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.CurrencyConversionDTO;
import com.primebuild_online.model.DTO.ExchangeRateApiResponseDTO;
import com.primebuild_online.model.ExchangeRate;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.Currency;
import com.primebuild_online.model.enumerations.NotificationType;
import com.primebuild_online.repository.ExchangeRateRepository;
import com.primebuild_online.security.SecurityUtils;
import com.primebuild_online.service.ExchangeRateService;
import com.primebuild_online.service.NotificationService;
import com.primebuild_online.service.UserService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;
    private final NotificationService notificationService;
    private final UserService userService;

    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository,
                                   RestTemplate restTemplate,
                                   NotificationService notificationService,
                                   UserService userService) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.restTemplate = restTemplate;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    private User loggedInUser() {
        return userService.getUserById(
                Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId()
        );
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
        exchangeRate.setRate(Math.round(lkrRate * 100.0) / 100.0);
        exchangeRate.setLastUpdated(LocalDateTime.now());
        exchangeRate.setDate(today);

        LocalDate yesterday = LocalDate.now().minusDays(1);
        ExchangeRate yesterdayExchangeRate = exchangeRateRepository.getExchangeRateByDate(yesterday);

        if (yesterdayExchangeRate != null) {
            double yesterdayLkrRate = yesterdayExchangeRate.getRate();
            exchangeRateNotification(lkrRate, yesterdayLkrRate);
        }

        return exchangeRateRepository.save(exchangeRate);
    }

    private void exchangeRateNotification(Double todayLkrRate, Double yesterdayLkrRate) {
        double difference = Math.round((yesterdayLkrRate - todayLkrRate) * 100.0) / 100.0;

        if (difference > 0) {
            notificationService.createNotification(
                    "Exchange Rate Update",
                    "USD rate decreased by Rs " + String.format("%.2f", Math.abs(difference)),
                    NotificationType.EXCHANGE_RATE,
                    loggedInUser()
            );
        }
        if (difference < 0) {
            notificationService.createNotification(
                    "Exchange Rate Update",
                    "USD rate increased by Rs " +  String.format("%.2f", Math.abs(difference)),
                    NotificationType.EXCHANGE_RATE,
                    loggedInUser()
            );
        }


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

    @Override
    public List<ExchangeRate> getExchangeRateListDaysBefore(Long days) {

        LocalDate today = LocalDate.now();
        LocalDate fiveDaysAgo = today.minusDays(days);

        return exchangeRateRepository.findAllByDateBetween(fiveDaysAgo, today);
    }


}
