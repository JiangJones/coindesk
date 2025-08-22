# CoinDesk API 專案

## 📋 專案概述

本專案為**國泰世華銀行 Java Engineer 線上作業**，使用 Spring Boot 框架開發 RESTful API，實現 CoinDesk API 串接與幣別資料管理功能。

## 🛠 技術規格

- **Java**: 8
- **Framework**: Spring Boot 2.7.18
- **Build Tool**: Maven
- **Database**: H2 (In-Memory)
- **ORM**: Spring Data JPA
- **測試**: JUnit 5 + Mockito

## 📁 專案結構

```
coindesk-api/
├── pom.xml                          # Maven 配置檔
├── README.md                        # 專案說明文件
├── src/
│   ├── main/
│   │   ├── java/com/cathay/coindeskapi/
│   │   │   ├── CoinDeskApiApplication.java    # 主程式
│   │   │   ├── config/                        # 配置類
│   │   │   ├── controller/                    # REST 控制器
│   │   │   ├── dto/                          # 資料傳輸物件
│   │   │   ├── entity/                       # JPA 實體
│   │   │   ├── repository/                   # 資料訪問層
│   │   │   └── service/                      # 業務邏輯層
│   │   └── resources/
│   │       ├── application.properties        # 應用程式配置
│   │       └── data.sql                     # 初始化資料
│   └── test/                               # 測試檔案
```

## 🚀 快速開始

### 環境需求
- Java 8 或以上版本
- Maven 3.6 或以上版本

### 啟動步驟
```bash
# 1. clone專案
git clone https://github.com/JiangJones/coindesk.git
cd coindesk

# 2. 編譯專案
mvn clean compile

# 3. 啟動應用程式
mvn spring-boot:run
```

### 驗證啟動
應用程式啟動後，可訪問以下端點驗證：
- http://localhost:8080/api/currencies
- http://localhost:8080/h2-console (H2 資料庫控制台)

## 🧪 測試執行

### 單元測試執行
```bash
# 執行所有單元測試
mvn test

# 僅執行特定測試類別
mvn test -Dtest=CoinDeskServiceTest
```

**測試涵蓋範圍：**
- ✅ **資料轉換邏輯單元測試** - 時間格式轉換、幣別名稱對應
- ✅ **CRUD API 功能測試** - 幣別資料增刪改查
- ✅ **CoinDesk API 整合測試** - 外部 API 呼叫與資料處理
- ✅ **邊界情況測試** - 異常處理、空值處理

### API 功能測試

**瀏覽器測試：**
啟動專案後，直接在瀏覽器訪問 API 端點進行測試。

**Chrome 開發者工具測試：**
在 Chrome 瀏覽器中按 F12 開啟開發者工具，切換至 Console 分頁

```javascript
//提醒： 某些瀏覽器可能需要先手動允許貼上程式碼如下:
allow pasting //允許瀏覽器貼上程式碼

// 查詢所有幣別
fetch('http://localhost:8080/api/currencies') .then(response => response.json()) .then(data => { console.log('所有幣別:', data); });

// 查詢美元
fetch('http://localhost:8080/api/currencies/USD') .then(response => response.json()) .then(data => { console.log('USD 幣別資訊:', data); });

// 新增韓元
fetch('http://localhost:8080/api/currencies', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ code: 'KRW', chineseName: '韓元' }) }) .then(response => response.json()) .then(data => { console.log('新增成功:', data); });

// 更新韓元名稱
fetch('http://localhost:8080/api/currencies/KRW', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ code: 'KRW', chineseName: '南韓韓元' }) }) .then(response => response.json()) .then(data => { console.log('更新成功:', data); });

// 刪除韓元
fetch('http://localhost:8080/api/currencies/KRW', { method: 'DELETE' }) .then(response => { if(response.ok) { console.log('刪除成功'); } });
```

## 📚 API 文件

### 幣別管理 API

| 方法 | 端點 | 說明 | 範例 |
|------|------|------|------|
| GET | `/api/currencies` | 查詢所有幣別 | 取得完整幣別清單 |
| GET | `/api/currencies/{code}` | 查詢特定幣別 | `/api/currencies/USD` |
| POST | `/api/currencies` | 新增幣別 | 新增幣別資料 |
| PUT | `/api/currencies/{code}` | 更新幣別 | 更新幣別中文名稱 |
| DELETE | `/api/currencies/{code}` | 刪除幣別 | 移除指定幣別 |

### CoinDesk API

| 方法 | 端點 | 說明 |
|------|------|------|
| GET | `/api/coindesk/original` | 取得原始 CoinDesk 資料 |
| GET | `/api/coindesk/transformed` | 取得轉換後的資料格式 |

## 💡 功能特色

### 1. **幣別資料管理**
- 完整的 CRUD 操作
- 資料驗證與錯誤處理
- 自動時間戳記更新

### 2. **CoinDesk API 整合**
- 即時匯率資料取得
- 時間格式標準化 (ISO 8601 → yyyy/MM/dd HH:mm:ss)
- 中英文幣別名稱對應

### 3. **資料庫設計**
- 使用 **JPA Entity** 自動建立資料表結構
- **data.sql** 提供初始化資料
- **H2 資料庫** 支援記憶體內快速存取

**資料表欄位說明：**
- `id`: 主鍵，自動遞增
- `code`: 幣別代碼 (如 USD, EUR)，唯一鍵
- `chinese_name`: 幣別中文名稱
- `created_time`, `updated_time`: 時間戳記

## 📋 作業需求對照

| 需求項目 | 實作狀態 | 說明 |
|----------|----------|------|
| Maven 建置 | ✅ | 使用 Maven 作為建置工具 |
| JDK 8 | ✅ | 支援 Java 8 |
| Spring Boot | ✅ | 基於 Spring Boot 2.7.18 |
| H2 資料庫 | ✅ | 內嵌式 H2 + Spring Data JPA |
| 幣別 CRUD API | ✅ | 完整實作查詢/新增/修改/刪除功能 |
| CoinDesk API 串接 | ✅ | 整合外部 API 並進行資料轉換 |
| 單元測試 | ✅ | 涵蓋核心業務邏輯測試 |
| 資料轉換測試 | ✅ | 針對資料轉換邏輯作單元測試 |
| CRUD API 測試 | ✅ | 測試幣別對應表資料 CRUD API |
| CoinDesk API 測試 | ✅ | 測試呼叫 CoinDesk API |
| 轉換 API 測試 | ✅ | 測試資料轉換的 API |

## 📞 聯絡資訊

如有任何問題，歡迎透過 email 聯繫。
---

**開發者**: JiangJones  
**專案用途**: 國泰世華銀行 Java Engineer 線上作業  
**完成日期**: 2025年8月22號
