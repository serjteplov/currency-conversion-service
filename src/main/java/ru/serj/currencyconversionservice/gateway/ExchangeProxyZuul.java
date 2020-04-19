package ru.serj.currencyconversionservice.gateway;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.serj.currencyconversionservice.model.CurrencyConversionBean;

@FeignClient(name = "zuul-gateway-server")
@RibbonClient(name = "zuul-gateway-server")
public interface ExchangeProxyZuul {

    @GetMapping("/currency-exchange-service/currency-exchange/{from}/to/{to}")
    CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}
