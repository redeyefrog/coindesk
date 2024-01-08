package org.redeyefrog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyQueryConditionRequest {

    @JsonProperty("CURRENCY")
    private String currency;

    @JsonProperty("CURRENCY_NAME")
    private String currencyName;

}
