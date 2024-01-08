package org.redeyefrog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CurrencyResponse extends CommonResponse {

    @JsonProperty("Currency")
    private CurrencyDto currency;

}
