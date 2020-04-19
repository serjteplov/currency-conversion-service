package ru.serj.currencyconversionservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.serj.currencyconversionservice.gateway.ExchangeProxyDiscovery;
import ru.serj.currencyconversionservice.gateway.ExchangeProxyUri;
import ru.serj.currencyconversionservice.gateway.ExchangeProxyZuul;
import ru.serj.currencyconversionservice.model.CurrencyConversionBean;
import ru.serj.currencyconversionservice.util.InstanceInformationService;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("currency-converter/")
public class ConversionController {

    @Autowired
    private ExchangeProxyZuul proxyZuul;

    @Autowired
    private ExchangeProxyDiscovery proxyDiscovery;

    @Autowired
    private ExchangeProxyUri proxyUri;

    @Value("${currency.exchange.service.service.host}") // kubernetes engine automatically
    private String exchangeHost;                        // injects CURRENCY_EXCHANGE_SERVICE_SERVICE_HOST into container

    @Value("${currency.exchange.service.service.port}")
    private String port;

    @Autowired
    InstanceInformationService informationService;

    @GetMapping("healthz")
    public String health() {
        return "Ok";
    }

    @GetMapping("from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyRestTemplate(@PathVariable String from, @PathVariable String to,
                                                              @PathVariable BigDecimal quantity) {
        log.info("Received Request to convert from {} {} to {}. ", quantity, from, to);
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversionBean> responseEntity =
                new RestTemplate().getForEntity("http://" + exchangeHost + ":" + port + "/currency-exchange/{from}/to/{to}",
                        CurrencyConversionBean.class, uriVariables);
        CurrencyConversionBean resp = responseEntity.getBody();
        return new CurrencyConversionBean(resp.getId(),
                from,
                to,
                resp.getConversionMultiple(),
                quantity,
                quantity.multiply(resp.getConversionMultiple()),
                resp.getPort(),resp.getExchangeEnvironmentInfo(), informationService.retrieveInstanceInfo());
    }

    @GetMapping("zuul/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeignZuul(@PathVariable String from, @PathVariable String to,
                                                           @PathVariable BigDecimal quantity) {
        CurrencyConversionBean resp = proxyZuul.retrieveExchangeValue(from, to);
        log.info("resp:{}", resp);
        return new CurrencyConversionBean(resp.getId(),
                from,
                to,
                resp.getConversionMultiple(),
                quantity,
                quantity.multiply(resp.getConversionMultiple()),
                resp.getPort(), resp.getExchangeEnvironmentInfo(), informationService.retrieveInstanceInfo());
    }

    @GetMapping("discovery/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeignEureka(@PathVariable String from, @PathVariable String to,
                                                           @PathVariable BigDecimal quantity) {
        CurrencyConversionBean resp = proxyDiscovery.retrieveExchangeValue(from, to);
        log.info("resp:{}", resp);
        return new CurrencyConversionBean(resp.getId(),
                from,
                to,
                resp.getConversionMultiple(),
                quantity,
                quantity.multiply(resp.getConversionMultiple()),
                resp.getPort(), resp.getExchangeEnvironmentInfo(), informationService.retrieveInstanceInfo());
    }

    @GetMapping("uri/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeignUri(@PathVariable String from, @PathVariable String to,
                                                  @PathVariable BigDecimal quantity) {
        CurrencyConversionBean resp = proxyUri.retrieveExchangeValue(from, to);
        return new CurrencyConversionBean(resp.getId(),
                from,
                to,
                resp.getConversionMultiple(),
                quantity,
                quantity.multiply(resp.getConversionMultiple()),
                resp.getPort(), resp.getExchangeEnvironmentInfo(), informationService.retrieveInstanceInfo());
    }
}
