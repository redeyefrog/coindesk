package org.redeyefrog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CurrencyRequest {

    @NotBlank(message = "currency is blank")
    @JsonProperty("CURRENCY")
    private String currency;

    @JsonProperty("CURRENCY_NAME")
    private String currencyName;

}
