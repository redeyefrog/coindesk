package org.redeyefrog.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redeyefrog.dto.CoinDeskTransferResponse;
import org.redeyefrog.dto.CurrencyDto;
import org.redeyefrog.dto.CurrencyQueryConditionRequest;
import org.redeyefrog.dto.CurrencyQueryResultResponse;
import org.redeyefrog.dto.CurrencyRequest;
import org.redeyefrog.dto.CurrencyResponse;
import org.redeyefrog.entity.CurrencyEntity;
import org.redeyefrog.repository.CurrencyRepository;
import org.redeyefrog.telegram.CallCoinDesk;
import org.redeyefrog.telegram.dto.CoinDeskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CoinDeskService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CallCoinDesk callTelegram;

    public CurrencyQueryResultResponse findCurrency(CurrencyQueryConditionRequest request) {
        ExampleMatcher matcher = ExampleMatcher.matching();
        CurrencyEntity.CurrencyEntityBuilder builder = CurrencyEntity.builder();
        if (StringUtils.isNotBlank(request.getCurrency())) {
            builder.currency(request.getCurrency());
        }
        if (StringUtils.isNotBlank(request.getCurrencyName())) {
            builder.currencyName(request.getCurrencyName());
            matcher = matcher.withMatcher("currencyName", match -> match.contains());
        }

        List<CurrencyEntity> currencyEntityList = currencyRepository.findAll(Example.of(builder.build(), matcher));
        return CurrencyQueryResultResponse.builder()
                                          .resultDesc("SUCCESS")
                                          .currencyList(currencyEntityList.stream()
                                                                          .map(currency -> CurrencyDto.builder()
                                                                                                      .currency(currency.getCurrency())
                                                                                                      .currencyName(currency.getCurrencyName())
                                                                                                      .build())
                                                                          .collect(Collectors.toList()))
                                          .build();
    }

    public CurrencyResponse saveCurrency(CurrencyRequest request) {
        saveCurrency(CurrencyEntity.builder()
                                   .currency(request.getCurrency())
                                   .currencyName(request.getCurrencyName())
                                   .build());
        return CurrencyResponse.builder()
                               .resultDesc("SUCCESS")
                               .build();
    }

    public CurrencyResponse updateCurrency(CurrencyRequest request) {
        CurrencyEntity currencyEntity = saveCurrency(CurrencyEntity.builder()
                                                                   .currency(request.getCurrency())
                                                                   .currencyName(request.getCurrencyName())
                                                                   .build());
        return CurrencyResponse.builder()
                               .resultDesc("SUCCESS")
                               .currency(CurrencyDto.builder()
                                                    .currency(currencyEntity.getCurrency())
                                                    .currencyName(currencyEntity.getCurrencyName())
                                                    .build())
                               .build();
    }

    public CurrencyResponse deleteCurrency(CurrencyRequest request) {
        currencyRepository.deleteById(request.getCurrency());
        return CurrencyResponse.builder()
                               .resultDesc("SUCCESS")
                               .build();
    }

    public CoinDeskResponse getCoinDesk() {
        return callTelegram.call();
    }

    public CoinDeskTransferResponse getTransferCoinDesk() {
        CoinDeskResponse coinDeskResponse = getCoinDesk();
        Map<String, CurrencyEntity> currencyMap = currencyRepository.findAll()
                                                                    .stream().collect(Collectors.toMap(CurrencyEntity::getCurrency, Function.identity()));
        String updatedISO = coinDeskResponse.getTime().getUpdatedISO();
        return CoinDeskTransferResponse.builder()
                                       .updateTime(LocalDateTime.parse(coinDeskResponse.getTime().getUpdatedISO(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                                                                .atOffset(ZoneOffset.of(updatedISO.substring(updatedISO.indexOf("+"))))
                                                                .atZoneSameInstant(ZoneId.systemDefault())
                                                                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                                       .currencyList(getCurrencyList(coinDeskResponse, currencyMap))
                                       .build();
    }

    private CurrencyEntity saveCurrency(CurrencyEntity currencyEntity) {
        return currencyRepository.save(currencyEntity);
    }

    private List<CurrencyDto> getCurrencyList(CoinDeskResponse coinDeskResponse, Map<String, CurrencyEntity> currencyMap) {
        List<CurrencyDto> currencyList = new ArrayList<>();

        CoinDeskResponse.CoinDeskBpiContent usd = coinDeskResponse.getBpi().getUsd();
        currencyList.add(CurrencyDto.builder()
                                    .currency(usd.getCode())
                                    .currencyName(getCurrencyName(currencyMap.get(usd.getCode())))
                                    .currencyRate(usd.getRate())
                                    .build());

        CoinDeskResponse.CoinDeskBpiContent gbp = coinDeskResponse.getBpi().getGbp();
        currencyList.add(CurrencyDto.builder()
                                    .currency(gbp.getCode())
                                    .currencyName(getCurrencyName(currencyMap.get(gbp.getCode())))
                                    .currencyRate(gbp.getRate())
                                    .build());

        CoinDeskResponse.CoinDeskBpiContent eur = coinDeskResponse.getBpi().getEur();
        currencyList.add(CurrencyDto.builder()
                                    .currency(eur.getCode())
                                    .currencyName(getCurrencyName(currencyMap.get(eur.getCode())))
                                    .currencyRate(eur.getRate())
                                    .build());

        return currencyList;
    }

    private String getCurrencyName(CurrencyEntity currencyEntity) {
        return currencyEntity == null ? StringUtils.EMPTY : StringUtils.trimToEmpty(currencyEntity.getCurrencyName());
    }

}
