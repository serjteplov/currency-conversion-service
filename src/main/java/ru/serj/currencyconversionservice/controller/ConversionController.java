package ru.serj.currencyconversionservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.serj.currencyconversionservice.gateway.CurrencyExchangeServiceProxy;
import ru.serj.currencyconversionservice.model.CurrencyConversionBean;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@Slf4j
public class ConversionController {

    @Autowired
    private CurrencyExchangeServiceProxy proxy;

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal quantity) {


        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversionBean> responseEntity =
                new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/{from}/to/{to}",
                        CurrencyConversionBean.class, uriVariables);
        CurrencyConversionBean resp = responseEntity.getBody();

        return new CurrencyConversionBean(resp.getId(),
                from,
                to,
                resp.getConversionMultiple(),
                quantity,
                quantity.multiply(resp.getConversionMultiple()),
                resp.getPort());

    }


    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal quantity) {


        CurrencyConversionBean resp = proxy.retrieveExchangeValue(from, to);
        log.info("resp:{}", resp);
        return new CurrencyConversionBean(resp.getId(),
                from,
                to,
                resp.getConversionMultiple(),
                quantity,
                quantity.multiply(resp.getConversionMultiple()),
                resp.getPort());

    }
}
