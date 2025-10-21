# GongGong-SDK

[![Maven Central](https://img.shields.io/maven-central/v/io.github.tato126/GongGong-SDK)](https://central.sonatype.com/artifact/io.github.tato126/GongGong-SDK)
[![Java](https://img.shields.io/badge/Java-21+-blue.svg)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)

**GongGong-SDK** is a Java SDK that provides easy access to South Korea's public data APIs.

Currently supports the **Seoul Metro Real-time Arrival Information API**, with plans to add more public APIs in the future.

> **GongGong** (공공) means "public" in Korean.

---

## ✨ Features

- 🚇 **Seoul Metro Real-time Arrival** information
- 🔧 **Simple API** - Get started with just a few lines of code
- 🛡️ **Type-safe** - Strongly-typed objects for all responses
- 📝 **Fully Documented** - Complete JavaDoc for all methods
- ⚡ **High Performance** - Built on Java 21's native HttpClient
- 🔐 **Reliable** - Built-in exception handling and validation

---

## 📦 Installation

### Gradle

```gradle
dependencies {
    implementation 'io.github.tato126:GongGong-SDK:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.tato126</groupId>
    <artifactId>GongGong-SDK</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🚀 Quick Start

### 1. Get an API Key

Obtain an API key from [Seoul Open Data Plaza](https://data.seoul.go.kr/).
- **Sample Key**: Up to 5 results per request
- **Production Key**: Up to 1000 results per request

### 2. Write Code

```java
import io.github.tato126.config.ApiConfig;
import io.github.tato126.model.SubwayArrival;
import io.github.tato126.service.SubwayService;

import java.util.List;

public class Example {
    public static void main(String[] args) {
        // 1. Configure API
        ApiConfig config = new ApiConfig("YOUR_API_KEY");

        // 2. Create service
        SubwayService service = new SubwayService(config);

        // 3. Get real-time arrivals for Gangnam Station
        List<SubwayArrival> arrivals = service.getRealtimeArrival("강남");

        // 4. Display results
        arrivals.forEach(arrival -> {
            System.out.println("🚇 " + arrival.getTrainLineName());
            System.out.println("⏰ Arriving in " + arrival.getArrivalTimeInMinutes() + " minutes");
            System.out.println("📍 " + arrival.getLocationStatus());
            System.out.println("---");
        });
    }
}
```

### 3. Output

```
🚇 Bound for Seongsu - Yeoksam direction
⏰ Arriving in 2 minutes
📍 [Line 2] Gangnam - Arriving at Seolleung
---
🚇 Bound for Sinseol-dong - Euljiro 1-ga direction
⏰ Arriving in 5 minutes
📍 [Line 2] Seolleung - Entering Yeoksam
---
```

---

## 📖 Usage Guide

### Basic Query

```java
// Query with default settings (up to 100 results)
List<SubwayArrival> arrivals = service.getRealtimeArrival("강남");
```

### Pagination

```java
// Get first 5 results (for sample API key)
List<SubwayArrival> arrivals = service.getRealtimeArrival("강남", 0, 4);

// Get results from 10th to 20th
List<SubwayArrival> arrivals = service.getRealtimeArrival("강남", 10, 19);
```

### SubwayArrival Object

```java
SubwayArrival arrival = arrivals.get(0);

// Basic Information
arrival.getStationName();              // Station name: "강남" (Gangnam)
arrival.getSubwayLineId();             // Line ID: "1002" (Line 2)
arrival.getTrainLineName();            // Direction: "Bound for Seongsu - Yeoksam direction"

// Arrival Time Information
arrival.getArrivalTime();              // Time in seconds: "120"
arrival.getArrivalTimeInMinutes();     // Time in minutes: 2
arrival.getFirstArrivalMessage();      // Message: "[Line 2] Gangnam - Arriving at Seolleung"

// Location Information
arrival.getLocationStatus();           // Current status: "[Line 2] Gangnam - Arriving at Seolleung"
arrival.getCurrentStationId();         // Current station ID
arrival.getPreviousStationId();        // Previous station ID
arrival.getNextStationId();            // Next station ID

// Destination Information
arrival.getFinalDestinationStationName(); // Final destination: "Seongsu"
arrival.getUpDownLine();               // Direction: "Outer loop" or "Inner loop"

// Additional Information
arrival.isLastTrainOfDay();            // Last train: true/false
arrival.getTransferLineCount();        // Number of transfer lines
```

### Custom Timeouts

```java
import java.time.Duration;

ApiConfig config = ApiConfig.builder()
    .apiKey("YOUR_API_KEY")
    .connectTimeout(Duration.ofSeconds(10))  // Connection timeout
    .readTimeout(Duration.ofSeconds(30))     // Read timeout
    .build();

SubwayService service = new SubwayService(config);
```

### Exception Handling

```java
import io.github.tato126.exception.ApiException;
import io.github.tato126.exception.NetworkException;

try {
    List<SubwayArrival> arrivals = service.getRealtimeArrival("강남");
} catch (NetworkException e) {
    // Network errors (connection failure, timeout, etc.)
    System.err.println("Network error: " + e.getMessage());
} catch (ApiException e) {
    // API errors (invalid response, parsing failure, etc.)
    System.err.println("API error: " + e.getMessage());
} catch (IllegalArgumentException e) {
    // Invalid parameters (missing station name, etc.)
    System.err.println("Parameter error: " + e.getMessage());
}
```

---

## 🏗️ Architecture

```
io.github.tato126/
├── config/
│   └── ApiConfig          # API configuration (key, timeouts)
├── service/
│   └── SubwayService      # Subway API service
├── model/
│   ├── SubwayArrival      # Arrival information model
│   ├── SubwayLine         # Line information (Enum)
│   └── ArrivalCode        # Arrival codes (Enum)
├── http/
│   └── HttpClient         # HTTP communication client
└── exception/
    ├── ApiException       # API errors
    ├── NetworkException   # Network errors
    ├── AuthenticationException
    └── ValidationException
```

---

## 🌐 Supported APIs

### ✅ Currently Supported

- **Seoul Metro Real-time Arrival Information**
  - Lines: 1-9, Gyeongui-Jungang, Airport Railroad, Gyeongchun, Suin-Bundang, Shinbundang,
    Uijeongbu, Yongin Everline, Ui-Sinseol, Seohae, Gimpo Goldline, Sillim

### 🔜 Coming Soon

- Seoul Bus Arrival Information
- Nationwide Weather Information
- Korea Public Data Portal APIs
- (Suggestions welcome via Issues!)

---

## 🔧 Special Station Names

Some stations require special naming conventions:

```java
// ❌ Incorrect
service.getRealtimeArrival("응암순환");
service.getRealtimeArrival("공릉");

// ✅ Correct
service.getRealtimeArrival("응암순환(상선)");
service.getRealtimeArrival("공릉(서울산업대입구)");
service.getRealtimeArrival("천호(풍납토성)");
```

**Note**: Station names must be in Korean (Hangul) as required by the Seoul Open Data API.

---

## 📋 Requirements

- **Java**: 21 LTS or higher
- **Dependencies**:
  - Jackson 2.16.0 (JSON parsing)
  - SLF4J 2.0.9 (Logging facade)
  - Logback 1.4.11 (Logging implementation)

---

## 📄 License

This project is licensed under the [Apache License 2.0](LICENSE).

---

## 🤝 Contributing

Contributions are always welcome!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines

- Follow existing code style and conventions
- Add unit tests for new features
- Update JavaDoc for public APIs
- Ensure all tests pass before submitting PR

---

## 🐛 Bug Reports & Feature Requests

Please use [GitHub Issues](https://github.com/tato126/GongGong-SDK/issues) to report bugs or request features.

When reporting bugs, please include:
- Java version
- SDK version
- Detailed description of the issue
- Steps to reproduce
- Expected vs actual behavior

---

## 👤 Author

**Heechan** - [@tato126](https://github.com/tato126)

Project Link: [https://github.com/tato126/GongGong-SDK](https://github.com/tato126/GongGong-SDK)

---

## 🙏 Acknowledgments

- [Seoul Open Data Plaza](https://data.seoul.go.kr/) - Public API provider
- [Stripe Java SDK](https://github.com/stripe/stripe-java) - Architecture inspiration
- [OkHttp](https://github.com/square/okhttp) - Design pattern reference

---

## 📚 Resources

- [Seoul Metro Real-time Arrival API Documentation](https://data.seoul.go.kr/dataList/OA-12764/F/1/datasetView.do) (Korean)
- [Korea Public Data Portal](https://www.data.go.kr/)
- [JavaDoc](https://javadoc.io/doc/io.github.tato126/GongGong-SDK) (Coming soon)

---

## 🗺️ Roadmap

- [x] Seoul Metro real-time arrival information
- [ ] Seoul Bus real-time arrival information
- [ ] Weather API integration
- [ ] Public Data Portal integration
- [ ] English API documentation
- [ ] Performance optimization
- [ ] Caching layer
- [ ] Rate limiting support

---

## 💡 Why GongGong-SDK?

Korean public data APIs are powerful but can be challenging to use due to:
- Complex JSON response structures
- Inconsistent error handling
- Limited English documentation
- Varying authentication methods

**GongGong-SDK** abstracts away these complexities, providing a clean, type-safe, and well-documented interface for Java developers.

---

## 🌏 International Developers

While this SDK focuses on Korean public data APIs, we welcome international developers!

**Important Notes:**
- Station names must be in Korean (Hangul)
- API responses include Korean text
- You'll need a Korean API key from Seoul Open Data Plaza
- Consider using translation libraries if you need English station names

**Example with comments:**
```java
// "강남" = Gangnam Station (one of Seoul's busiest stations)
// "신림" = Sillim Station
// "잠실" = Jamsil Station

List<SubwayArrival> arrivals = service.getRealtimeArrival("강남");
```

---

<p align="center">Made with ❤️ in Seoul, South Korea</p>
<p align="center">서울에서 사랑을 담아 제작</p>
