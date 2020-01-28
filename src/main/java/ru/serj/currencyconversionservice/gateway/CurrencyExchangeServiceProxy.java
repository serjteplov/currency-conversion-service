package ru.serj.currencyconversionservice.gateway;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.serj.currencyconversionservice.model.CurrencyConversionBean;

//@FeignClient(name = "currency-exchange-service", url = "localhost:8000")
//@FeignClient(name = "currency-exchange-service")
@FeignClient(name = "zuul-gateway-server")
@RibbonClient(name = "zuul-gateway-server")
public interface CurrencyExchangeServiceProxy {

    //@GetMapping("/currency-exchange/{from}/to/{to}")
    @GetMapping("/currency-exchange-service/currency-exchange/{from}/to/{to}") // through the Zuul proxy
    CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}
