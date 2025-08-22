package com.cathay.coindeskapi.service;

import com.cathay.coindeskapi.dto.CoinDeskResponse;
import com.cathay.coindeskapi.dto.TransformedApiResponse;
import com.cathay.coindeskapi.entity.Currency;
import com.cathay.coindeskapi.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * CoinDeskService 單元測試
 * 主要測試資料轉換相關邏輯
 */
@ExtendWith(MockitoExtension.class)
public class CoinDeskServiceTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private CurrencyRepository currencyRepository;
    
    @InjectMocks
    private CoinDeskService coinDeskService;
    
    private CoinDeskResponse mockCoinDeskResponse;
    
    @BeforeEach
    void setUp() {
        // 準備測試資料
        mockCoinDeskResponse = createMockCoinDeskResponse();
    }
    
    /**
     * 測試時間格式轉換邏輯
     * ISO 8601 -> yyyy/MM/dd HH:mm:ss
     */
    @Test
    public void testConvertTimeFormat() {
        // Given - 給定 ISO 8601 時間格式
        String isoTime = "2023-12-01T10:30:00+00:00";
        
        // When - 執行時間格式轉換
        String result = coinDeskService.convertTimeFormat(isoTime);
        
        // Then - 驗證結果格式正確
        assertNotNull(result, "轉換結果不應為 null");
        assertTrue(result.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"), 
                   "時間格式應為 yyyy/MM/dd HH:mm:ss");
        assertEquals("2023/12/01 10:30:00", result, "時間轉換結果應正確");
    }
    
    /**
     * 測試不同時區的時間格式轉換
     */
    @Test
    public void testConvertTimeFormatWithDifferentTimezone() {
        // Given - 不同時區的時間
        String isoTimeWithTimezone = "2023-12-01T18:45:30+08:00";
        
        // When
        String result = coinDeskService.convertTimeFormat(isoTimeWithTimezone);
        
        // Then
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
    
    /**
     * 測試 CoinDesk API 呼叫功能
     */
    @Test
    public void testGetCoinDeskData() {
        // Given - Mock RestTemplate 回傳值
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockCoinDeskResponse);
        
        // When - 呼叫 CoinDesk API
        CoinDeskResponse result = coinDeskService.getCoinDeskData();
        
        // Then - 驗證回傳結果
        assertNotNull(result, "CoinDesk API 回應不應為 null");
        assertNotNull(result.getTime(), "時間資訊不應為 null");
        assertNotNull(result.getBpi(), "幣別資訊不應為 null");
        assertEquals(3, result.getBpi().size(), "應包含 3 種幣別");
    }
    
    /**
     * 測試完整的資料轉換邏輯
     * 這是最重要的測試 - 驗證整個轉換流程
     */
    @Test
    public void testGetTransformedData() {
        // Given - 準備 Mock 資料
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockCoinDeskResponse);
        
        // Mock 幣別資料庫查詢
        Currency usdCurrency = new Currency("USD", "美元");
        Currency eurCurrency = new Currency("EUR", "歐元");
        Currency gbpCurrency = new Currency("GBP", "英鎊");
        
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(usdCurrency));
        when(currencyRepository.findByCode("EUR")).thenReturn(Optional.of(eurCurrency));
        when(currencyRepository.findByCode("GBP")).thenReturn(Optional.of(gbpCurrency));
        
        // When - 執行資料轉換
        TransformedApiResponse result = coinDeskService.getTransformedData();
        
        // Then - 驗證轉換結果
        assertNotNull(result, "轉換結果不應為 null");
        
        // 驗證時間轉換
        assertNotNull(result.getUpdateTime(), "更新時間不應為 null");
        assertTrue(result.getUpdateTime().matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"),
                   "時間格式應正確");
        
        // 驗證幣別資料轉換
        assertNotNull(result.getCurrencies(), "幣別列表不應為 null");
        assertEquals(3, result.getCurrencies().size(), "應包含 3 種幣別");
        
        // 驗證具體幣別資料
        TransformedApiResponse.CurrencyDetail usdDetail = result.getCurrencies().stream()
                .filter(c -> "USD".equals(c.getCode()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(usdDetail, "應包含 USD 幣別");
        assertEquals("USD", usdDetail.getCode(), "幣別代碼應正確");
        assertEquals("美元", usdDetail.getChineseName(), "中文名稱應正確");
        assertEquals(50000.0, usdDetail.getRate(), "匯率應正確");
    }
    
    /**
     * 測試未知幣別的處理邏輯
     */
    @Test
    public void testGetTransformedDataWithUnknownCurrency() {
        // Given - 資料庫中找不到對應幣別
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockCoinDeskResponse);
        when(currencyRepository.findByCode(anyString())).thenReturn(Optional.empty());
        
        // When
        TransformedApiResponse result = coinDeskService.getTransformedData();
        
        // Then - 應使用預設名稱
        assertNotNull(result);
        result.getCurrencies().forEach(currency -> {
            assertEquals("未知幣別", currency.getChineseName(), 
                        "未知幣別應顯示預設名稱");
        });
    }
    
    /**
     * 測試空的 CoinDesk 回應處理
     */
    @Test
    public void testGetTransformedDataWithEmptyResponse() {
        // Given - 空的回應
        CoinDeskResponse emptyResponse = new CoinDeskResponse();
        CoinDeskResponse.TimeInfo timeInfo = new CoinDeskResponse.TimeInfo();
        timeInfo.setUpdatedISO("2023-12-01T10:30:00+00:00");
        emptyResponse.setTime(timeInfo);
        emptyResponse.setBpi(new HashMap<>());
        
        when(restTemplate.getForObject(anyString(), any())).thenReturn(emptyResponse);
        
        // When
        TransformedApiResponse result = coinDeskService.getTransformedData();
        
        // Then
        assertNotNull(result);
        assertTrue(result.getCurrencies().isEmpty(), "幣別列表應為空");
    }
    
    /**
     * 建立測試用的 Mock CoinDesk 回應資料
     */
    private CoinDeskResponse createMockCoinDeskResponse() {
        CoinDeskResponse response = new CoinDeskResponse();
        
        // 設定時間資訊
        CoinDeskResponse.TimeInfo timeInfo = new CoinDeskResponse.TimeInfo();
        timeInfo.setUpdated("Dec 1, 2023 10:30:00 UTC");
        timeInfo.setUpdatedISO("2023-12-01T10:30:00+00:00");
        timeInfo.setUpdatedUk("Dec 1, 2023 at 10:30 GMT");
        response.setTime(timeInfo);
        
        // 設定幣別資訊
        Map<String, CoinDeskResponse.CurrencyInfo> bpi = new HashMap<>();
        
        // USD
        CoinDeskResponse.CurrencyInfo usd = new CoinDeskResponse.CurrencyInfo();
        usd.setCode("USD");
        usd.setSymbol("&#36;");
        usd.setRate("50,000.0000");
        usd.setDescription("United States Dollar");
        usd.setRateFloat(50000.0);
        bpi.put("USD", usd);
        
        // EUR
        CoinDeskResponse.CurrencyInfo eur = new CoinDeskResponse.CurrencyInfo();
        eur.setCode("EUR");
        eur.setSymbol("&euro;");
        eur.setRate("45,000.0000");
        eur.setDescription("Euro");
        eur.setRateFloat(45000.0);
        bpi.put("EUR", eur);
        
        // GBP
        CoinDeskResponse.CurrencyInfo gbp = new CoinDeskResponse.CurrencyInfo();
        gbp.setCode("GBP");
        gbp.setSymbol("&pound;");
        gbp.setRate("40,000.0000");
        gbp.setDescription("British Pound Sterling");
        gbp.setRateFloat(40000.0);
        bpi.put("GBP", gbp);
        
        response.setBpi(bpi);
        response.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index");
        response.setChartName("Bitcoin");
        
        return response;
    }
}