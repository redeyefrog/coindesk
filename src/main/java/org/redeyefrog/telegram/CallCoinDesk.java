package org.redeyefrog.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redeyefrog.exception.CoinDeskRuntimeException;
import org.redeyefrog.telegram.dto.CoinDeskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class CallCoinDesk {

    @Value("${coindesk.currency.api.url}")
    private String coinDeskUrl;

    @Autowired
    private ObjectMapper objectMapper;

    public CoinDeskResponse call() {
        try {
            URL url = new URL(coinDeskUrl);
            InputStream in = url.openStream();
            String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            return objectMapper.readValue(text, CoinDeskResponse.class);
        } catch(Exception e) {
           throw new CoinDeskRuntimeException(e);
        }
    }

}
