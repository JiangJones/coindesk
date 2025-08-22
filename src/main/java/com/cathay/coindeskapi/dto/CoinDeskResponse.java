package com.cathay.coindeskapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class CoinDeskResponse {
    
    @JsonProperty("time")
    private TimeInfo time;
    
    @JsonProperty("disclaimer")
    private String disclaimer;
    
    @JsonProperty("chartName")
    private String chartName;
    
    @JsonProperty("bpi")
    private Map<String, CurrencyInfo> bpi;
    
    // 內部類別 - 時間資訊
    public static class TimeInfo {
        @JsonProperty("updated")
        private String updated;
        
        @JsonProperty("updatedISO")
        private String updatedISO;
        
        @JsonProperty("updateduk")
        private String updatedUk;
        
        // Getter 和 Setter
        public String getUpdated() { return updated; }
        public void setUpdated(String updated) { this.updated = updated; }
        public String getUpdatedISO() { return updatedISO; }
        public void setUpdatedISO(String updatedISO) { this.updatedISO = updatedISO; }
        public String getUpdatedUk() { return updatedUk; }
        public void setUpdatedUk(String updatedUk) { this.updatedUk = updatedUk; }
    }
    
    // 內部類別 - 幣別資訊
    public static class CurrencyInfo {
        @JsonProperty("code")
        private String code;
        
        @JsonProperty("symbol")
        private String symbol;
        
        @JsonProperty("rate")
        private String rate;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("rate_float")
        private Double rateFloat;
        
        // Getter 和 Setter
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
        public String getRate() { return rate; }
        public void setRate(String rate) { this.rate = rate; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Double getRateFloat() { return rateFloat; }
        public void setRateFloat(Double rateFloat) { this.rateFloat = rateFloat; }
    }
    
    // Getter 和 Setter
    public TimeInfo getTime() { return time; }
    public void setTime(TimeInfo time) { this.time = time; }
    public String getDisclaimer() { return disclaimer; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }
    public String getChartName() { return chartName; }
    public void setChartName(String chartName) { this.chartName = chartName; }
    public Map<String, CurrencyInfo> getBpi() { return bpi; }
    public void setBpi(Map<String, CurrencyInfo> bpi) { this.bpi = bpi; }
}