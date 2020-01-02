package ru.serj.currencyconversionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionBean {
    Long id;
    String from;
    String to;
    BigDecimal conversionMultiple;
    BigDecimal quantity;
    BigDecimal totalCalculated;
    int port;
}
