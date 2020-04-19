package ru.serj.currencyconversionservice.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.serj.currencyconversionservice.model.CurrencyConversionBean;

@FeignClient(name = "random-gang", url = "${currency.exchange.service.service.host}:8000") // kubernetes automatically injects CURRENCY_EXCHANGE_SERVICE_SERVICE_HOST into container
//@FeignClient(name = "random-gang", url = "${currency.exchange.service.uri}:8000") // from application.properties or deployment.yml
//@FeignClient(name = "random-gang", url = "${CURRENCY_EXCHANGE_SERVICE_URI}:8000") // from kuber configmap or from deployment.yml
public interface ExchangeProxyUri {

    @GetMapping("/currency-exchange/{from}/to/{to}")
    CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}
