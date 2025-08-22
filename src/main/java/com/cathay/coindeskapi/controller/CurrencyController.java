package com.cathay.coindeskapi.controller;

import com.cathay.coindeskapi.dto.CurrencyDto;
import com.cathay.coindeskapi.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {
    
    @Autowired
    private CurrencyService currencyService;
    
    // 查詢所有幣別
    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencies);
    }
    
    // 根據代碼查詢幣別
    @GetMapping("/{code}")
    public ResponseEntity<CurrencyDto> getCurrencyByCode(@PathVariable String code) {
        return currencyService.getCurrencyByCode(code)
                .map(currency -> ResponseEntity.ok(currency))
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 新增幣別
    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@RequestBody CurrencyDto currencyDto) {
        try {
            CurrencyDto created = currencyService.createCurrency(currencyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 更新幣別
    @PutMapping("/{code}")
    public ResponseEntity<CurrencyDto> updateCurrency(
            @PathVariable String code, 
            @RequestBody CurrencyDto currencyDto) {
        try {
            CurrencyDto updated = currencyService.updateCurrency(code, currencyDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 刪除幣別
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable String code) {
        try {
            currencyService.deleteCurrency(code);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}