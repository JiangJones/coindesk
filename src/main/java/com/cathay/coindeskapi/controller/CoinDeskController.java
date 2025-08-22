package com.cathay.coindeskapi.controller;

import com.cathay.coindeskapi.dto.CoinDeskResponse;
import com.cathay.coindeskapi.dto.TransformedApiResponse;
import com.cathay.coindeskapi.service.CoinDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coindesk")
public class CoinDeskController {
    
    @Autowired
    private CoinDeskService coinDeskService;
    
    // 呼叫原始 CoinDesk API
    @GetMapping("/original")
    public ResponseEntity<CoinDeskResponse> getOriginalCoinDeskData() {
        CoinDeskResponse data = coinDeskService.getCoinDeskData();
        return ResponseEntity.ok(data);
    }
    
    // 呼叫轉換後的 API
    @GetMapping("/transformed")
    public ResponseEntity<TransformedApiResponse> getTransformedData() {
        TransformedApiResponse data = coinDeskService.getTransformedData();
        return ResponseEntity.ok(data);
    }
}