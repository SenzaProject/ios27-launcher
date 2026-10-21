# iOS 27 Launcher

**A SenzaProject concept for an iOS-inspired Android home screen.**

> This is an independent Android launcher inspired by iOS visual language. It is not affiliated with, sponsored by, or endorsed by Apple Inc.

## Identity
- **App name:** iOS 27
- **Developer:** SenzaProject
- **Version:** 1.0 (Beta Test)
- **Application ID:** `com.senzaproject.ios27`
- **Minimum Android:** Android 6.0 (API 23)
- **Build system:** Gradle + Kotlin

## Current features
- Can be selected as the Android default Home app
- Gradient background and clean iOS-inspired visual styling
- Clock and date header
- Installed-app grid with app icons
- Search/filter installed apps
- Tap an app icon to open the app
- Custom vector app icon

## Build
Use Android Studio with JDK 17 and Android SDK Platform 35:

```bash
git clone https://github.com/USERNAME/ios27-launcher.git
cd ios27-launcher
gradle assembleDebug
```

The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Termux notes
Termux alone does not automatically include the Android SDK. The simplest workflow is:
1. Install Git in Termux: `pkg update && pkg install git`
2. Clone this repository.
3. Push source code to GitHub from Termux.
4. Build the APK with Android Studio, or use the included GitHub Actions workflow.

## Push to GitHub
Create an empty repository on GitHub, then run:

```bash
git init
git add .
git commit -m "Initial release: iOS 27 Launcher 1.0 Beta"
git branch -M main
git remote add origin https://github.com/USERNAME/ios27-launcher.git
git push -u origin main
```

Replace `USERNAME` with your GitHub username.

## Roadmap
- Configurable wallpaper and icon grid
- Widget support
- Dock customization
- App drawer categories
- Settings and accessibility refinements

## About
iOS 27 Launcher by SenzaProject brings a refined, iOS-inspired visual experience to Android. With a clean home screen, fluid-inspired aesthetics, searchable apps, and streamlined navigation, it reimagines everyday use with a fresh and modern identity.

**Enjoy a fresh way to navigate.**
