package org.redeyefrog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class CurrencyQueryResultResponse extends CommonResponse {

    @JsonProperty("CURRENCY_LIST")
    private List<CurrencyDto> currencyList;

}
