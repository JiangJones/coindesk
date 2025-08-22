package com.cathay.coindeskapi.service;

import com.cathay.coindeskapi.entity.Currency;
import com.cathay.coindeskapi.dto.CurrencyDto;
import com.cathay.coindeskapi.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    
    @Autowired
    private CurrencyRepository currencyRepository;
    
    // 查詢所有幣別
    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // 根據代碼查詢幣別
    public Optional<CurrencyDto> getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code)
                .map(this::convertToDto);
    }
    
    // 新增幣別
    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        if (currencyRepository.existsByCode(currencyDto.getCode())) {
            throw new RuntimeException("Currency code already exists: " + currencyDto.getCode());
        }
        
        Currency currency = new Currency(currencyDto.getCode(), currencyDto.getChineseName());
        Currency saved = currencyRepository.save(currency);
        return convertToDto(saved);
    }
    
    // 更新幣別
    public CurrencyDto updateCurrency(String code, CurrencyDto currencyDto) {
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + code));
        
        currency.setChineseName(currencyDto.getChineseName());
        Currency updated = currencyRepository.save(currency);
        return convertToDto(updated);
    }
    
    // 刪除幣別
    public void deleteCurrency(String code) {
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + code));
        currencyRepository.delete(currency);
    }
    
    // Entity 轉 DTO
    private CurrencyDto convertToDto(Currency currency) {
        return new CurrencyDto(currency.getCode(), currency.getChineseName());
    }
}