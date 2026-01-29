# WebCraft Android Setup Guide

This document provides instructions for setting up the WebCraft Android project for development and deployment.

## Prerequisites

- **Android Studio**: Latest stable version (Hedgehog 2023.1.1 or later)
- **JDK**: Java Development Kit 17 or higher
- **Android SDK**: API Level 35 (Android 16) or higher
- **Gradle**: 8.9 (included via wrapper)

## Initial Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd WebCraft
```

### 2. Install Android SDK Components

Open Android Studio and ensure the following SDK components are installed via **SDK Manager**:
- Android SDK Platform 35
- Android SDK Build-Tools 35.0.0 or later
- Android Emulator (for testing)

### 3. Sync Gradle

Open the project in Android Studio and let Gradle sync automatically. If it doesn't start automatically:
1. Go to **File** → **Sync Project with Gradle Files**
2. Wait for dependencies to download

## Firebase Configuration

### Setting Up Firebase

1. **Create a Firebase Project**:
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Click "Add project" or select an existing project
   - Enter project name (e.g., "WebCraft")
   - Enable Google Analytics (recommended)

2. **Add Android App to Firebase**:
   - Click "Add app" and select Android
   - Enter package name: `com.trebedit.webcraft`
   - (Optional) Enter app nickname: "WebCraft"
   - Download the `google-services.json` file

3. **Replace Placeholder Configuration**:
   - Copy the downloaded `google-services.json` to `app/google-services.json`
   - The placeholder file in the repository is for build purposes only
   - **Never commit your real `google-services.json` to version control**

4. **Enable Firebase Services**:
   - In Firebase Console, enable:
     - **Crashlytics**: For crash reporting
     - **Analytics**: For user analytics
     - **Remote Config**: For feature flags and A/B testing

## AdMob Configuration

### Setting Up AdMob

1. **Create AdMob Account**:
   - Go to [AdMob Console](https://apps.admob.com/)
   - Sign in with your Google account
   - Create a new app or select existing app

2. **Generate Ad Unit IDs**:
   - In AdMob Console, go to **Apps** → **Add app** (if new)
   - Create ad units for different placements:
     - **Banner Ads**: For bottom banner
     - **Interstitial Ads**: For full-screen ads
     - **Rewarded Ads**: For incentivized content
     - **Native Ads**: For in-feed ads

3. **Update AndroidManifest.xml**:
   - Open `app/src/main/AndroidManifest.xml`
   - Replace the placeholder AdMob App ID:
     ```xml
     <meta-data
         android:name="com.google.android.gms.ads.APPLICATION_ID"
         android:value="ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY" />
     ```
   - Use your real AdMob App ID (format: `ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY`)

4. **Test Ads During Development**:
   - Use Google's test Ad Unit IDs during development:
     - Banner: `ca-app-pub-3940256099942544/6300978111`
     - Interstitial: `ca-app-pub-3940256099942544/1033173712`
     - Rewarded: `ca-app-pub-3940256099942544/5224354917`
   - Switch to production Ad Unit IDs before release

## Google Play Console Setup

### Preparing for Release

1. **Create Google Play Developer Account**:
   - Go to [Google Play Console](https://play.google.com/console)
   - Pay one-time registration fee ($25 USD)
   - Complete account setup

2. **Create App in Play Console**:
   - Click "Create app"
   - Enter app details:
     - **App name**: WebCraft
     - **Default language**: English (US)
     - **App or game**: App
     - **Free or paid**: Free
   - Accept declarations and create app

3. **Configure App Information**:
   - **App details**: Description, category, contact details
   - **Store listing**: Screenshots, feature graphic, icon
   - **Content rating**: Complete questionnaire
   - **Privacy policy**: Provide URL to privacy policy

## Signing Configuration

### Generating Release Keystore

1. **Create Keystore File**:
   ```bash
   keytool -genkey -v -keystore app/keystore/release.keystore \
     -alias webcraft -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Secure Keystore Credentials**:
   - Store keystore password securely (e.g., password manager)
   - Never commit keystore to version control
   - Set environment variables for CI/CD:
     ```bash
     export KEYSTORE_PASSWORD="your_keystore_password"
     export KEY_ALIAS="webcraft"
     export KEY_PASSWORD="your_key_password"
     ```

3. **Update Signing Config** (if needed):
   - The `app/build.gradle.kts` already has placeholder signing config
   - Update keystore path if using different location

## Building the App

### Debug Build

Build a debug APK for testing:

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build

Build a release APK:

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

### Build Android App Bundle (AAB)

For Play Store upload:

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

## Running the App

### On Emulator

1. **Create AVD** (Android Virtual Device):
   - Open **AVD Manager** in Android Studio
   - Click "Create Virtual Device"
   - Select device (e.g., Pixel 6)
   - Choose system image: API 35 (Android 16)
   - Finish and start emulator

2. **Run App**:
   ```bash
   ./gradlew installDebug
   ```

### On Physical Device

1. **Enable Developer Options**:
   - Go to **Settings** → **About phone**
   - Tap **Build number** 7 times

2. **Enable USB Debugging**:
   - Go to **Settings** → **Developer options**
   - Enable **USB debugging**

3. **Connect Device**:
   - Connect via USB cable
   - Accept debugging authorization on device

4. **Install and Run**:
   ```bash
   ./gradlew installDebug
   ```

## Testing

### Unit Tests

Run unit tests:

```bash
./gradlew test
```

### Instrumented Tests

Run Android instrumented tests:

```bash
./gradlew connectedAndroidTest
```

### Lint Checks

Run lint checks:

```bash
./gradlew lint
```

## Continuous Integration

### GitHub Actions Example

Create `.github/workflows/android.yml`:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    - name: Build with Gradle
      run: ./gradlew build
    - name: Run tests
      run: ./gradlew test
```

## Environment Variables

For production builds, set these environment variables:

```bash
# Signing
export KEYSTORE_PASSWORD="your_keystore_password"
export KEY_ALIAS="webcraft"
export KEY_PASSWORD="your_key_password"

# Firebase (if not using google-services.json)
export FIREBASE_PROJECT_ID="your_project_id"
export FIREBASE_API_KEY="your_api_key"
```

## Troubleshooting

### Build Failures

1. **Dependency Resolution Issues**:
   - Clear Gradle cache: `./gradlew clean`
   - Delete `.gradle` folder and sync again
   - Invalidate caches in Android Studio: **File** → **Invalidate Caches / Restart**

2. **SDK Version Mismatch**:
   - Ensure Android SDK Platform 35 is installed
   - Update Android SDK Build-Tools via SDK Manager

3. **Out of Memory**:
   - Increase Gradle memory in `gradle.properties`:
     ```
     org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
     ```

### Firebase Issues

1. **google-services.json Not Found**:
   - Ensure file is in `app/` directory
   - Check file is valid JSON
   - Re-download from Firebase Console if corrupted

2. **Firebase Initialization Failed**:
   - Verify package name matches in Firebase Console
   - Check SHA-1 fingerprint is added to Firebase project:
     ```bash
     keytool -list -v -keystore app/keystore/release.keystore
     ```

### AdMob Issues

1. **Ads Not Showing**:
   - Use test Ad Unit IDs during development
   - Verify AdMob App ID in `AndroidManifest.xml`
   - Check internet permissions are granted

2. **Invalid Ad Request**:
   - Ensure device is not in ad blocking mode
   - Check AdMob account is active and verified

## Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Documentation](https://firebase.google.com/docs)
- [AdMob Documentation](https://developers.google.com/admob)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)

## Support

For issues or questions:
- Open an issue on GitHub
- Contact: [your-email@example.com]

## License

[Your License Here]
