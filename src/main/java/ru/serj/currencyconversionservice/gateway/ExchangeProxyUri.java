package ru.serj.currencyconversionservice.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.serj.currencyconversionservice.model.CurrencyConversionBean;

// при исользовании имени random gang, вызов метода не будет трассироваться и логироваться sleuth
@FeignClient(name = "random-gang", url = "${currency.exchange.service.service.host}:8000") // from kubernetes engine (automatically injects CURRENCY_EXCHANGE_SERVICE_SERVICE_HOST into container)
//@FeignClient(name = "random-gang", url = "${currency.exchange.service.uri}:8000") // from application.properties or deployment.yml
public interface ExchangeProxyUri {

    @GetMapping("/currency-exchange/{from}/to/{to}")
    CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}
