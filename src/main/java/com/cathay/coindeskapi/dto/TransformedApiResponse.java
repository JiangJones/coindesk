package com.cathay.coindeskapi.dto;

import java.util.List;

public class TransformedApiResponse {
    private String updateTime;
    private List<CurrencyDetail> currencies;
    
    // 內部類別 - 幣別詳情
    public static class CurrencyDetail {
        private String code;
        private String chineseName;
        private Double rate;
        
        public CurrencyDetail() {}
        
        public CurrencyDetail(String code, String chineseName, Double rate) {
            this.code = code;
            this.chineseName = chineseName;
            this.rate = rate;
        }
        
        // Getter 和 Setter
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getChineseName() { return chineseName; }
        public void setChineseName(String chineseName) { this.chineseName = chineseName; }
        public Double getRate() { return rate; }
        public void setRate(Double rate) { this.rate = rate; }
    }
    
    // Getter 和 Setter
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
    public List<CurrencyDetail> getCurrencies() { return currencies; }
    public void setCurrencies(List<CurrencyDetail> currencies) { this.currencies = currencies; }
}