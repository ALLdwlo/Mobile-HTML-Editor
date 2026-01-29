# WebCraft Android Project Structure

This document provides an overview of the complete project structure for the WebCraft Android application.

## Project Overview

- **Project Name**: WebCraft
- **Package Name**: `com.trebedit.webcraft`
- **Min SDK**: 35 (Android 16)
- **Target SDK**: 35 (Android 16)
- **Compile SDK**: 35
- **Kotlin Version**: 2.0.21
- **Gradle Version**: 8.9
- **AGP Version**: 8.7.3

## Directory Structure

```
WebCraft/
├── .git/                           # Git version control
├── .gitignore                      # Git ignore patterns
├── README.md                       # Project documentation
├── PROJECT_STRUCTURE.md            # This file
├── build.gradle.kts                # Root build configuration
├── settings.gradle.kts             # Project settings
├── gradle.properties               # Gradle properties
├── gradlew                         # Gradle wrapper script (Unix)
├── gradlew.bat                     # Gradle wrapper script (Windows)
│
├── gradle/
│   ├── libs.versions.toml          # Version catalog (dependency versions)
│   └── wrapper/
│       ├── gradle-wrapper.jar      # Gradle wrapper JAR
│       └── gradle-wrapper.properties
│
├── docs/
│   └── SETUP.md                    # Detailed setup instructions
│
└── app/
    ├── build.gradle.kts            # App module build configuration
    ├── proguard-rules.pro          # ProGuard/R8 rules
    ├── google-services.json        # Firebase configuration (placeholder)
    │
    ├── keystore/
    │   └── README.md               # Instructions for keystore
    │
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   │
        │   ├── java/com/trebedit/webcraft/
        │   │   ├── WebCraftApplication.kt    # Application class
        │   │   ├── MainActivity.kt           # Main activity
        │   │   └── ui/theme/
        │   │       ├── Color.kt              # Theme colors
        │   │       ├── Type.kt               # Typography
        │   │       └── Theme.kt              # Material 3 theme
        │   │
        │   └── res/
        │       ├── drawable/
        │       │   └── ic_launcher_foreground.xml
        │       ├── mipmap-anydpi-v26/
        │       │   ├── ic_launcher.xml
        │       │   └── ic_launcher_round.xml
        │       ├── values/
        │       │   ├── colors.xml
        │       │   ├── strings.xml
        │       │   ├── themes.xml
        │       │   └── ic_launcher_background.xml
        │       └── xml/
        │           ├── backup_rules.xml
        │           └── data_extraction_rules.xml
        │
        ├── test/
        │   └── java/com/trebedit/webcraft/
        │       └── ExampleUnitTest.kt
        │
        └── androidTest/
            └── java/com/trebedit/webcraft/
                └── ExampleInstrumentedTest.kt
```

## Key Components

### 1. Build Configuration Files

#### `build.gradle.kts` (Root)
- Configures common build logic
- Applies plugins for all modules
- Defines clean task

#### `app/build.gradle.kts`
- Android application configuration
- SDK versions (min: 35, target: 35, compile: 35)
- Build types (debug, release)
- Signing configuration (optional, keystore-based)
- ProGuard/R8 configuration
- Jetpack Compose setup
- All project dependencies

#### `settings.gradle.kts`
- Plugin management repositories
- Dependency resolution management
- Repository mirrors configured for Google and Maven Central
- Includes app module

#### `gradle/libs.versions.toml`
- Centralized version catalog
- All dependency versions pinned
- Plugin versions defined
- Library bundles (optional, not used yet)

### 2. Application Code

#### `WebCraftApplication.kt`
- Application entry point
- Firebase initialization
- AdMob SDK initialization

#### `MainActivity.kt`
- Main activity using Jetpack Compose
- Edge-to-edge display support
- Material 3 Scaffold
- Simple welcome screen

#### UI Theme (`ui/theme/`)
- **Color.kt**: Material 3 color definitions
- **Type.kt**: Typography system
- **Theme.kt**: Dynamic color scheme support (Android 12+)

### 3. Resources

#### Manifest (`AndroidManifest.xml`)
- App permissions (Internet, Network State)
- Application configuration
- Activity declarations
- AdMob App ID metadata (placeholder)

#### Drawables & Icons
- Adaptive icons with foreground/background layers
- Vector drawable support
- Placeholder launcher icons

#### Values
- **strings.xml**: App name and description
- **colors.xml**: Base color palette
- **themes.xml**: App theme configuration

#### XML Configuration
- **backup_rules.xml**: Backup exclusions
- **data_extraction_rules.xml**: Data transfer rules

### 4. ProGuard/R8 Rules (`proguard-rules.pro`)

Configured optimizations for:
- Kotlin standard library
- Kotlinx Coroutines
- Kotlinx Serialization
- Jetpack Compose
- AndroidX Room
- OkHttp
- JSoup
- Firebase services
- Google Play Services
- Billing library

### 5. Testing

#### Unit Tests (`test/`)
- JUnit 4 tests
- Example unit test included

#### Instrumented Tests (`androidTest/`)
- Android JUnit tests
- Espresso UI tests
- Compose UI tests
- Example instrumented test included

## Dependencies

### Core Dependencies

#### Kotlin & Coroutines
- kotlin-stdlib: 2.0.21
- kotlinx-coroutines-core: 1.9.0
- kotlinx-coroutines-android: 1.9.0
- kotlinx-serialization-json: 1.7.3

#### AndroidX Core
- core-ktx: 1.15.0
- appcompat: 1.7.0
- lifecycle-runtime-ktx: 2.8.7
- lifecycle-viewmodel-compose: 2.8.7
- activity-compose: 1.9.3

#### Jetpack Compose
- Compose BOM: 2024.12.01
- compose-ui
- compose-material3
- compose-runtime
- compose-ui-tooling (debug)
- navigation-compose: 2.8.5

#### Data & Storage
- datastore-preferences: 1.1.1
- room-runtime: 2.6.1
- room-ktx: 2.6.1
- room-compiler (KSP): 2.6.1

#### Networking
- okhttp: 4.12.0
- jsoup: 1.18.3

#### Google Services
- Firebase BOM: 33.7.0
  - firebase-crashlytics-ktx
  - firebase-analytics-ktx
  - firebase-remote-config-ktx
- play-services-ads: 23.6.0
- billing-ktx: 7.1.1

#### Testing
- junit: 4.13.2
- androidx-test-ext-junit: 1.2.1
- androidx-test-espresso-core: 3.6.1
- compose-ui-test-junit4

## Build Variants

### Debug Build
- Application ID: `com.trebedit.webcraft.debug`
- Debuggable: Yes
- Minification: Disabled
- Resource shrinking: Disabled
- Signing: Debug keystore (auto-generated)

### Release Build
- Application ID: `com.trebedit.webcraft`
- Debuggable: No
- Minification: Enabled (R8)
- Resource shrinking: Enabled
- Signing: Custom keystore (optional, if exists)
- ProGuard rules applied

## Firebase Configuration

### `google-services.json`
- Placeholder configuration included
- Real configuration must be obtained from Firebase Console
- Required services:
  - Crashlytics (crash reporting)
  - Analytics (user analytics)
  - Remote Config (feature flags)

### Setup Steps
1. Create Firebase project
2. Add Android app with package name `com.trebedit.webcraft`
3. Download `google-services.json`
4. Replace placeholder file in `app/` directory

## AdMob Configuration

### Current Setup
- Placeholder test App ID in `AndroidManifest.xml`
- Test Ad Unit IDs should be used during development

### Production Setup
1. Create AdMob account
2. Generate real App ID
3. Create Ad Units for different placements
4. Update `AndroidManifest.xml` with production App ID
5. Implement ad loading logic in app code

## Signing Configuration

### Debug Build
- Uses auto-generated debug keystore
- Located at: `~/.android/debug.keystore`

### Release Build
- Custom keystore required for production
- Location: `app/keystore/release.keystore`
- Configuration via environment variables:
  - `KEYSTORE_PASSWORD`
  - `KEY_ALIAS`
  - `KEY_PASSWORD`
- Signing is optional if keystore doesn't exist

### Generate Release Keystore

```bash
keytool -genkey -v -keystore app/keystore/release.keystore \
  -alias webcraft -keyalg RSA -keysize 2048 -validity 10000
```

## Gradle Tasks

### Build Tasks
```bash
./gradlew clean                 # Clean build outputs
./gradlew build                 # Build debug and release APKs
./gradlew assembleDebug        # Build debug APK
./gradlew assembleRelease      # Build release APK
./gradlew bundleRelease        # Build release AAB for Play Store
```

### Installation Tasks
```bash
./gradlew installDebug         # Install debug APK on connected device
./gradlew installRelease       # Install release APK on connected device
./gradlew uninstallAll         # Uninstall all variants
```

### Testing Tasks
```bash
./gradlew test                 # Run unit tests
./gradlew testDebugUnitTest    # Run debug unit tests
./gradlew connectedAndroidTest # Run instrumented tests
./gradlew lint                 # Run lint checks
./gradlew lintDebug            # Run lint on debug variant
```

### Dependency Tasks
```bash
./gradlew dependencies         # Show dependency tree
./gradlew dependencyInsight    # Insight into specific dependency
```

## Next Steps

### Immediate Tasks
1. Replace `google-services.json` with real Firebase configuration
2. Update AdMob App ID in `AndroidManifest.xml`
3. Generate release keystore for signing
4. Test build process on local machine

### Development Tasks
1. Implement HTML editor UI
2. Add syntax highlighting library
3. Create file management system
4. Implement preview functionality
5. Add export/import features
6. Integrate AdMob ads
7. Set up in-app billing (if needed)

### CI/CD Setup
1. Configure GitHub Actions or GitLab CI
2. Add automated builds
3. Set up unit test execution
4. Configure Play Store deployment
5. Add code quality checks

## Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Firebase Documentation](https://firebase.google.com/docs)
- [AdMob Integration Guide](https://developers.google.com/admob)
- [Setup Guide](docs/SETUP.md)

## License

[Your License Here]

---

**Last Updated**: January 29, 2026
**Version**: 1.0.0
