package org.redeyefrog.controller;

import org.redeyefrog.dto.CoinDeskTransferResponse;
import org.redeyefrog.dto.CurrencyQueryConditionRequest;
import org.redeyefrog.dto.CurrencyQueryResultResponse;
import org.redeyefrog.dto.CurrencyRequest;
import org.redeyefrog.dto.CurrencyResponse;
import org.redeyefrog.service.CoinDeskService;
import org.redeyefrog.telegram.dto.CoinDeskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/coinDesk")
@RestController
public class CoinDeskController {

    @Autowired
    private CoinDeskService coinDeskService;

    @GetMapping("/findCurrency")
    public CurrencyQueryResultResponse findCurrency(@RequestBody @Valid CurrencyQueryConditionRequest request) {
        return coinDeskService.findCurrency(request);
    }

    @PostMapping("/insertCurrency")
    public CurrencyResponse insertCurrency(@RequestBody @Valid CurrencyRequest request) {
        return coinDeskService.saveCurrency(request);
    }

    @PutMapping("/updateCurrency")
    public CurrencyResponse updateCurrency(@RequestBody @Valid CurrencyRequest request) {
        return coinDeskService.updateCurrency(request);
    }

    @DeleteMapping("/deleteCurrency")
    public CurrencyResponse deleteCurrency(@RequestBody @Valid CurrencyRequest request) {
        return coinDeskService.deleteCurrency(request);
    }

    @GetMapping("/api")
    public CoinDeskResponse getCoinDesk() {
        return coinDeskService.getCoinDesk();
    }

    @GetMapping("/api/transfer")
    public CoinDeskTransferResponse getTransferCoinDesk() {
        return coinDeskService.getTransferCoinDesk();
    }

}
