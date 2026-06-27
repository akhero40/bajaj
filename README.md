# BFHL REST API — Spring Boot

A Spring Boot REST API for the Bajaj Finserv Health Qualifier Round.

## Endpoint

| Method | Route   | Status |
|--------|---------|--------|
| POST   | `/bfhl` | 200    |

---

## ⚡ Quick Start (local)

```bash
# 1. Update your personal details in:
#    src/main/resources/application.properties

# 2. Build & run
mvn clean spring-boot:run

# 3. Test
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["a","1","334","4","R","$"]}'
```

---

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
app.user.full-name=your_full_name       # e.g. john_doe
app.user.dob=ddmmyyyy                   # e.g. 17091999
app.user.email=your@email.com
app.user.roll-number=YOUR_ROLL_NUMBER
```

---

## 🚀 Deploy to Railway

1. Push this repo to GitHub
2. Go to [railway.app](https://railway.app) → New Project → Deploy from GitHub
3. Railway auto-detects the `Dockerfile` and builds it
4. Set environment variables in Railway dashboard (optional, overrides properties file):
   ```
   APP_USER_FULL-NAME=your_full_name
   APP_USER_DOB=ddmmyyyy
   APP_USER_EMAIL=your@email.com
   APP_USER_ROLL-NUMBER=YOUR_ROLL
   ```
5. Your URL will be: `https://your-app.up.railway.app/bfhl`

## 🚀 Deploy to Render

1. Push to GitHub
2. New Web Service → Connect repo
3. Runtime: **Docker**
4. Start command is handled by Dockerfile
5. URL: `https://your-app.onrender.com/bfhl`

---

## 📨 Sample Requests & Responses

### Example A
```json
// Request
{ "data": ["a", "1", "334", "4", "R", "$"] }

// Response
{
  "is_success": true,
  "user_id": "john_doe_17091999",
  "email": "john@xyz.com",
  "roll_number": "ABCD123",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

### Example B
```json
// Request
{ "data": ["2","a","y","4","&","-","*","5","92","b"] }

// Response
{
  "is_success": true,
  "user_id": "john_doe_17091999",
  "email": "john@xyz.com",
  "roll_number": "ABCD123",
  "odd_numbers": ["5"],
  "even_numbers": ["2", "4", "92"],
  "alphabets": ["A", "Y", "B"],
  "special_characters": ["&", "-", "*"],
  "sum": "103",
  "concat_string": "ByA"
}
```

### Example C
```json
// Request
{ "data": ["A", "ABCD", "DOE"] }

// Response
{
  "is_success": true,
  "user_id": "john_doe_17091999",
  "email": "john@xyz.com",
  "roll_number": "ABCD123",
  "odd_numbers": [],
  "even_numbers": [],
  "alphabets": ["A", "ABCD", "DOE"],
  "special_characters": [],
  "sum": "0",
  "concat_string": "EoDdCbAa"
}
```

---

## 🏗️ Project Structure

```
src/
├── main/java/com/bfhl/api/
│   ├── BfhlApiApplication.java          # Entry point
│   ├── controller/
│   │   └── BfhlController.java          # POST /bfhl
│   ├── service/
│   │   ├── BfhlService.java             # Interface
│   │   └── BfhlServiceImpl.java         # Implementation
│   ├── dto/
│   │   ├── BfhlRequestDto.java          # Request DTO
│   │   ├── BfhlResponseDto.java         # Response DTO
│   │   └── ErrorResponseDto.java        # Error DTO
│   └── exception/
│       └── GlobalExceptionHandler.java  # Error handling
└── test/java/com/bfhl/api/
    ├── BfhlServiceImplTest.java          # Unit tests (8 cases)
    └── BfhlControllerIntegrationTest.java # Integration tests (4 cases)
```

---

## ✅ Run Tests

```bash
mvn test
```
