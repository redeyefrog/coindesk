package org.redeyefrog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CoinDeskTransferResponse {

    @JsonProperty("UPDATE_TIME")
    private String updateTime;

    @JsonProperty("CURRENCY_LIST")
    private List<CurrencyDto> currencyList;

}
