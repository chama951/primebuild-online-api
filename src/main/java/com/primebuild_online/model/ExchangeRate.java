package com.primebuild_online.model;

import com.primebuild_online.model.enumerations.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "exchange_rate")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_currency")
    private Currency fromCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency")
    private Currency toCurrency;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "date")
    private LocalDate date;
}
