# WebCraft - Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### Prerequisites
- Android Studio (latest version)
- Java 17+
- Android SDK API 35

### Step 1: Clone & Open
```bash
git clone <repository-url>
cd WebCraft
```

Open the project in Android Studio: `File` → `Open` → Select WebCraft folder

### Step 2: Sync Dependencies
Android Studio will automatically sync Gradle dependencies. If not:
- Click `File` → `Sync Project with Gradle Files`
- Wait for dependencies to download (first time may take a few minutes)

### Step 3: Run the App
1. Connect an Android device or start an emulator
2. Click the green "Run" button in Android Studio
3. Select your device
4. Wait for build to complete

That's it! The app should launch with a welcome screen.

## 📱 What You Get

- ✅ **Modern Android App**: Targeting Android 16 (API 35)
- ✅ **Jetpack Compose**: Latest UI toolkit
- ✅ **Material 3**: Google's latest design system
- ✅ **Firebase Ready**: Analytics, Crashlytics, Remote Config
- ✅ **AdMob Ready**: Monetization configured
- ✅ **Room Database**: Local storage ready
- ✅ **Kotlin Coroutines**: Async operations built-in

## 🔧 Basic Commands

### Build
```bash
./gradlew assembleDebug       # Build debug APK
./gradlew assembleRelease     # Build release APK
./gradlew bundleRelease       # Build AAB for Play Store
```

### Test
```bash
./gradlew test                # Run unit tests
./gradlew lint                # Run code quality checks
```

### Install
```bash
./gradlew installDebug        # Install on connected device
```

## 📚 Next Steps

1. **Configure Firebase** (Optional but recommended)
   - Visit [Firebase Console](https://console.firebase.google.com/)
   - Add your Android app
   - Download `google-services.json`
   - Replace `app/google-services.json`

2. **Configure AdMob** (For monetization)
   - Visit [AdMob Console](https://apps.admob.com/)
   - Create ad units
   - Update `AndroidManifest.xml` with your AdMob App ID

3. **Start Development**
   - Implement HTML editor UI
   - Add syntax highlighting
   - Create file management
   - Build preview functionality

## 📖 Full Documentation

- [README.md](README.md) - Project overview
- [SETUP.md](docs/SETUP.md) - Detailed setup instructions
- [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) - Complete project structure
- [BUILD_CHECKLIST.md](BUILD_CHECKLIST.md) - Build verification checklist

## 🆘 Troubleshooting

### "SDK not found"
Install Android SDK API 35 via Android Studio SDK Manager

### "Gradle sync failed"
1. Check internet connection
2. Clear Gradle cache: `./gradlew clean`
3. Invalidate caches: `File` → `Invalidate Caches / Restart`

### "Build failed"
1. Ensure Java 17 is installed: `java -version`
2. Update Gradle wrapper: `./gradlew wrapper --gradle-version 8.9`
3. Check Android SDK is installed

## 💡 Tips

- Use debug build for development (faster build times)
- Enable R8 optimization only for release builds
- Test on real devices for accurate performance
- Use Android Studio's Logcat for debugging

## 🎯 Project Status

- ✅ **Foundation Complete**: All basic setup done
- 🚧 **Features Pending**: Editor UI, syntax highlighting, file management
- 📋 **Ready For**: Active development

---

**Need Help?** Check the full documentation or open an issue on GitHub.

Happy Coding! 🎉
