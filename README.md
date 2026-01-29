# WebCraft - Mobile HTML Editor

A modern Android application for editing HTML, built with Jetpack Compose and targeting Android 16+ (API 35+).

## Features (Planned)

- 🎨 Modern Material 3 Design
- 📝 HTML code editor with syntax highlighting
- 👀 Live preview of HTML content
- 💾 Local storage with Room database
- ☁️ Cloud backup with Firebase
- 📊 Analytics and crash reporting
- 💰 AdMob integration
- 🛡️ HTML sanitization with JSoup

## Tech Stack

- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: (To be added)
- **Database**: Room
- **Networking**: OkHttp
- **Async**: Kotlin Coroutines & Flow
- **Serialization**: Kotlinx Serialization
- **Analytics**: Firebase Analytics & Crashlytics
- **Monetization**: Google AdMob & In-App Billing

## Requirements

- **Android Studio**: Hedgehog 2023.1.1 or later
- **JDK**: 17 or higher
- **Min SDK**: 35 (Android 16)
- **Target SDK**: 35 (Android 16)
- **Compile SDK**: 35

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd WebCraft
```

### 2. Configure Firebase

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app with package name: `com.trebedit.webcraft`
3. Download `google-services.json`
4. Place it in `app/google-services.json`

### 3. Configure AdMob

1. Create an AdMob account at [AdMob Console](https://apps.admob.com/)
2. Create an app and generate Ad Unit IDs
3. Update `AndroidManifest.xml` with your AdMob App ID

### 4. Build the Project

```bash
./gradlew build
```

### 5. Run on Device/Emulator

```bash
./gradlew installDebug
```

## Project Structure

```
WebCraft/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/trebedit/webcraft/
│   │   │   │   ├── data/           # Data layer (repositories, data sources)
│   │   │   │   ├── domain/         # Domain layer (use cases, models)
│   │   │   │   ├── presentation/   # UI layer (composables, view models)
│   │   │   │   ├── ui/             # UI theme and components
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── WebCraftApplication.kt
│   │   │   ├── res/                # Resources (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                   # Unit tests
│   │   └── androidTest/            # Instrumented tests
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── google-services.json        # Firebase config (not in repo)
├── gradle/
│   └── libs.versions.toml          # Version catalog
├── docs/
│   └── SETUP.md                    # Detailed setup guide
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Documentation

For detailed setup instructions, see [SETUP.md](docs/SETUP.md).

### Available Documentation

- [Setup Guide](docs/SETUP.md) - Complete setup and configuration guide
- Architecture (Coming soon)
- Contributing (Coming soon)
- API Documentation (Coming soon)

## Building

### Debug Build

```bash
./gradlew assembleDebug
```

### Release Build

```bash
./gradlew assembleRelease
```

### Android App Bundle (for Play Store)

```bash
./gradlew bundleRelease
```

## Testing

### Run Unit Tests

```bash
./gradlew test
```

### Run Instrumented Tests

```bash
./gradlew connectedAndroidTest
```

### Run Lint Checks

```bash
./gradlew lint
```

## Code Quality

The project uses:
- **ProGuard/R8**: Code shrinking and obfuscation
- **Kotlin Lint**: Static code analysis
- **Detekt**: (To be added) Kotlin code style checker

## CI/CD

Continuous integration and deployment pipelines will be configured using:
- GitHub Actions (or GitLab CI)
- Automated builds on push
- Unit tests and lint checks
- Release to Play Store (internal track)

## Dependencies

### Core
- Kotlin 2.0.21
- Kotlinx Coroutines 1.9.0
- Kotlinx Serialization 1.7.3

### AndroidX
- Core KTX 1.15.0
- AppCompat 1.7.0
- Lifecycle 2.8.7
- Activity Compose 1.9.3
- Room 2.6.1
- DataStore 1.1.1

### Compose
- Compose BOM 2024.12.01
- Material 3
- Navigation Compose 2.8.5

### Networking
- OkHttp 4.12.0
- JSoup 1.18.3

### Google Services
- Firebase BOM 33.7.0
- Play Services Ads 23.6.0
- Billing KTX 7.1.1

See [libs.versions.toml](gradle/libs.versions.toml) for complete list.

## License

[Your License Here]

## Contributors

- [Your Name]

## Acknowledgments

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase](https://firebase.google.com/)
- [Material Design 3](https://m3.material.io/)

## Contact

For support or inquiries:
- Email: [your-email@example.com]
- Issues: [GitHub Issues](https://github.com/yourusername/webcraft/issues)

---

Made with ❤️ for Android developers
