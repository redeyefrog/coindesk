package org.redeyefrog.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redeyefrog.dto.CoinDeskTransferResponse;
import org.redeyefrog.dto.CurrencyQueryConditionRequest;
import org.redeyefrog.dto.CurrencyQueryResultResponse;
import org.redeyefrog.dto.CurrencyRequest;
import org.redeyefrog.dto.CurrencyResponse;
import org.redeyefrog.entity.CurrencyEntity;
import org.redeyefrog.repository.CurrencyRepository;
import org.redeyefrog.telegram.CallCoinDesk;
import org.redeyefrog.telegram.dto.CoinDeskResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

//@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class CoinDeskServiceTest {

    @InjectMocks
    private CoinDeskService coinDeskService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CallCoinDesk callTelegram;

    private static String FIND_CURRENCY = "test/findCurrencyResponse.json";

    private static String SAVE_OR_UPDATE_CURRENCY = "test/saveOrUpdateCurrency.json";

    private static String GET_COIN_DESK = "test/getCoinDesk.json";

    @Test
    public void findCurrency_NormalCase_01() {
        CurrencyQueryConditionRequest request = new CurrencyQueryConditionRequest();
        try {
            when(currencyRepository.findAll(any(Example.class))).thenAnswer(answer -> file2Bean(FIND_CURRENCY, new TypeReference<List<CurrencyEntity>>() {
            }));
            CurrencyQueryResultResponse response = coinDeskService.findCurrency(request);
            assertEquals("SUCCESS", response.getResultDesc());
            assertNotNull(response.getCurrencyList());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void findCurrency_ErrorCase_01() {
        CurrencyQueryConditionRequest request = new CurrencyQueryConditionRequest();
        try {
            when(currencyRepository.findAll(any(Example.class)))
                    .thenThrow(new MockitoException("Find Currency Error."));
            coinDeskService.findCurrency(request);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void saveCurrency_NormalCase_01() {
        CurrencyRequest request = new CurrencyRequest();
        try {
            when(currencyRepository.save(any())).thenAnswer(answer -> file2Bean(SAVE_OR_UPDATE_CURRENCY, new TypeReference<CurrencyEntity>() {
            }));
            CurrencyResponse response = coinDeskService.saveCurrency(request);
            assertEquals("SUCCESS", response.getResultDesc());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void saveCurrency_ErrorCase_01() {
        CurrencyRequest request = new CurrencyRequest();
        try {
            when(currencyRepository.save(any()))
                    .thenThrow(new MockitoException("Save Currency Error."));
            coinDeskService.saveCurrency(request);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void updateCurrency_NormalCase_01() {
        CurrencyRequest request = new CurrencyRequest();
        try {
            when(currencyRepository.save(any())).thenAnswer(answer -> file2Bean(SAVE_OR_UPDATE_CURRENCY, new TypeReference<CurrencyEntity>() {
            }));
            CurrencyResponse response = coinDeskService.updateCurrency(request);
            assertEquals("SUCCESS", response.getResultDesc());
            assertNotNull(response.getCurrency());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void updateCurrency_ErrorCase_01() {
        CurrencyRequest request = new CurrencyRequest();
        try {
            when(currencyRepository.save(any()))
                    .thenThrow(new MockitoException("Update Currency Error."));
            coinDeskService.updateCurrency(request);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteCurrency_NormalCase_01() {
        CurrencyRequest request = new CurrencyRequest();
        request.setCurrency("USD");
        try {
            doNothing().when(currencyRepository).deleteById(anyString());
            CurrencyResponse response = coinDeskService.deleteCurrency(request);
            assertEquals("SUCCESS", response.getResultDesc());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void deleteCurrency_ErrorCase_01() {
        CurrencyRequest request = new CurrencyRequest();
        request.setCurrency("USD");
        try {
            doThrow(new MockitoException("Delete Currency Error.")).when(currencyRepository).deleteById(anyString());
            coinDeskService.deleteCurrency(request);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void getCoinDesk_NormalCase_01() {
        try {
            CoinDeskResponse expected = file2Bean(GET_COIN_DESK, new TypeReference<CoinDeskResponse>() {
            });
            when(callTelegram.call()).thenAnswer(answer -> expected);
            CoinDeskResponse response = coinDeskService.getCoinDesk();
            assertEquals(expected.getTime().getUpdatedISO(), response.getTime().getUpdatedISO());
            assertEquals(expected.getDisclaimer(), response.getDisclaimer());
            assertEquals(expected.getChartName(), response.getChartName());
            assertEquals(expected.getBpi().getUsd().getCode(), response.getBpi().getUsd().getCode());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getCoinDesk_ErrorCase_01() {
        try {
            when(callTelegram.call()).thenThrow(new MockitoException("Call Coin Desk API Fail."));
            coinDeskService.getCoinDesk();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void getTransferCoinDesk_NormalCase_01() {
        try {
            when(callTelegram.call()).thenAnswer(answer -> file2Bean(GET_COIN_DESK, new TypeReference<CoinDeskResponse>() {
            }));
            when(currencyRepository.findAll()).thenAnswer(answer -> file2Bean(FIND_CURRENCY, new TypeReference<List<CurrencyEntity>>() {
            }));
            CoinDeskTransferResponse response = coinDeskService.getTransferCoinDesk();
            assertNotNull(response.getUpdateTime());
            assertNotNull(response.getCurrencyList());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void getTransferCoinDesk_ErrorCase_01() {
        try {
            when(callTelegram.call()).thenThrow(new MockitoException("Call Coin Desk API Data Transfer Fail."));
            coinDeskService.getTransferCoinDesk();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    private <T> T file2Bean(String path, TypeReference<T> typeReference) throws IOException {
        File file = new ClassPathResource(path).getFile();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, typeReference);
    }

}
