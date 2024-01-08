package org.redeyefrog.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CoinDeskResponse {

    private CoinDeskTime time;

    private String disclaimer;

    private String chartName;

    private CoinDeskBpi bpi;

    @Getter
    @Setter
    public static class CoinDeskTime {

        private String updated;

        private String updatedISO;

        private String updateduk;

    }

    @Getter
    @Setter
    public static class CoinDeskBpi {

        @JsonProperty("USD")
        private CoinDeskBpiContent usd;

        @JsonProperty("GBP")
        private CoinDeskBpiContent gbp;

        @JsonProperty("EUR")
        private CoinDeskBpiContent eur;

    }

    @Getter
    @Setter
    public static class CoinDeskBpiContent {

        private String code;

        private String symbol;

        private String rate;

        private String description;

        @JsonProperty("rate_float")
        private BigDecimal rateFloat;

    }

}
