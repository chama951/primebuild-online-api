package com.primebuild_online.repository;

import com.primebuild_online.model.ExchangeRate;
import com.primebuild_online.model.enumerations.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findByFromCurrencyAndToCurrencyAndDate(Currency currency, Currency currency1, LocalDate today);
    List<ExchangeRate> findAllByDateBetween(LocalDate fiveDaysAgo, LocalDate today);

    ExchangeRate getExchangeRateByDate(LocalDate date);
}
