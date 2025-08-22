## 環境要求
### JDK 安裝
1. 下載 Eclipse Temurin OpenJDK 8
   - 官網: https://adoptium.net/
   - 選擇: JDK 8 (LTS) - Windows x64 .msi 安裝檔

### Maven 安裝
1. 下載 Maven
   - 官網: https://maven.apache.org/download.cgi
   - 選擇: `apache-maven-3.9.9-bin.zip` (Binary zip archive)

2. 解壓縮到指定目錄
   ```bash
   C:\Program Files\Apache\maven
設定環境變數
新增系統變數:
變數名: M2_HOME
變數值: C:\Program Files\Apache\maven
編輯 Path 變數，新增: %M2_HOME%\bin

專案結構
coindesk-api/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/cathay/coindeskapi/
│   │   │       ├── config/
│   │   │       │   ├── CorsConfig.java
│   │   │       │   └── RestTemplateConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── CoinDeskController.java
│   │   │       │   └── CurrencyController.java
│   │   │       ├── dto/
│   │   │       │   ├── CoinDeskResponse.java
│   │   │       │   ├── CurrencyDto.java
│   │   │       │   └── TransformedApiResponse.java
│   │   │       ├── entity/
│   │   │       │   └── Currency.java
│   │   │       ├── repository/
│   │   │       │   └── CurrencyRepository.java
│   │   │       ├── service/
│   │   │       │   ├── CoinDeskService.java
│   │   │       │   └── CurrencyService.java
│   │   │       └── CoinDeskApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       └── java/
│           └── com/cathay/coindeskapi/
│               ├── controller/
│               └── service/
└── target/

快速啟動
開啟終端機或命令提示字元，執行以下指令：
mvn clean compile
mvn spring-boot:run
在瀏覽器中訪問以下網址，即可看到幣別列表：
http://localhost:8080/api/currencies

API
GET	/api/currencies	取得所有幣別
GET	/api/currencies/{code}	取得特定幣別資訊
POST	/api/currencies	新增幣別
PUT	/api/currencies/{code}	更新幣別資訊
DELETE	/api/currencies/{code}	刪除幣別
GET	/api/coindesk	取得原始 CoinDesk API 數據
GET	/api/coindesk/transformed	取得轉換後的 API 數據


瀏覽器測試
在 Chrome 開發者工具 (F12) 的 Console 中執行以下指令進行測試：

查詢所有幣別
fetch('http://localhost:8080/api/currencies')
  .then(response => response.json())
  .then(data => console.log('所有幣別:', data));
查詢美元
fetch('http://localhost:8080/api/currencies/USD')
  .then(response => response.json())
  .then(data => console.log('USD 幣別資訊:', data));

新增韓元
fetch('http://localhost:8080/api/currencies', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ code: 'KRW', chineseName: '韓元' })
})
.then(response => response.json())
.then(data => console.log('新增成功:', data));

更新韓元名稱
fetch('http://localhost:8080/api/currencies/KRW', {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ code: 'KRW', chineseName: '南韓韓元' })
})
.then(response => response.json())
.then(data => console.log('更新成功:', data));
