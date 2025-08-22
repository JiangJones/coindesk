package com.cathay.coindeskapi.dto;

public class CurrencyDto {
    private String code;
    private String chineseName;
    
    public CurrencyDto() {}
    
    public CurrencyDto(String code, String chineseName) {
        this.code = code;
        this.chineseName = chineseName;
    }
    
    // Getter 和 Setter
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getChineseName() { return chineseName; }
    public void setChineseName(String chineseName) { this.chineseName = chineseName; }
}