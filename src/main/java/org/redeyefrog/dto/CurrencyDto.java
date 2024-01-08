package org.redeyefrog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CurrencyDto {

    @JsonProperty("CURRENCY")
    private String currency;

    @JsonProperty("CURRENCY_NAME")
    private String currencyName;

    @JsonProperty("CURRENCY_RATE")
    private String currencyRate;

}
