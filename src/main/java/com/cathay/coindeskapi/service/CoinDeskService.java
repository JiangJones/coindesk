package com.cathay.coindeskapi.service;

import com.cathay.coindeskapi.dto.CoinDeskResponse;
import com.cathay.coindeskapi.dto.TransformedApiResponse;
import com.cathay.coindeskapi.entity.Currency;
import com.cathay.coindeskapi.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoinDeskService {
    
    private static final String COINDESK_API_URL = "https://kengp3.github.io/blog/coindesk.json";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private CurrencyRepository currencyRepository;
    
    // 呼叫 CoinDesk API
    public CoinDeskResponse getCoinDeskData() {
        return restTemplate.getForObject(COINDESK_API_URL, CoinDeskResponse.class);
    }
    
    // 資料轉換並組成新 API
    public TransformedApiResponse getTransformedData() {
        CoinDeskResponse coinDeskData = getCoinDeskData();
        
        TransformedApiResponse response = new TransformedApiResponse();
        
        // 轉換時間格式
        String formattedTime = convertTimeFormat(coinDeskData.getTime().getUpdatedISO());
        response.setUpdateTime(formattedTime);
        
        // 轉換幣別資訊
        List<TransformedApiResponse.CurrencyDetail> currencies = new ArrayList<>();
        
        coinDeskData.getBpi().forEach((key, value) -> {
            String code = value.getCode();
            String chineseName = getChineseName(code);
            Double rate = value.getRateFloat();
            
            currencies.add(new TransformedApiResponse.CurrencyDetail(code, chineseName, rate));
        });
        
        response.setCurrencies(currencies);
        return response;
    }
    
    // 時間格式轉換：ISO 8601 -> yyyy/MM/dd HH:mm:ss
    public String convertTimeFormat(String isoTime) {
        OffsetDateTime dateTime = OffsetDateTime.parse(isoTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return dateTime.format(formatter);
    }
    
    // 取得幣別中文名稱
    private String getChineseName(String code) {
        Optional<Currency> currency = currencyRepository.findByCode(code);
        return currency.map(Currency::getChineseName).orElse("未知幣別");
    }
}