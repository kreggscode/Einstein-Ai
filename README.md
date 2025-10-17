<div align="center">

# 🧠 Einstein AI

### *Discover Genius Wisdom Through Stunning Design*

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen?style=for-the-badge)](https://android-arsenal.com/api?level=24)

*A visually stunning Android application featuring Albert Einstein's profound quotes and wisdom with edge-to-edge glassmorphism design, AI-powered chat, and modern UI.*

[Features](#-features) • [Screenshots](#-screenshots) • [Tech Stack](#-tech-stack) • [Installation](#-installation) • [Contributing](#-contributing)

---

</div>

## ✨ Features

<table>
<tr>
<td width="50%">

### 🎨 **Stunning Design**

- 🌌 **Cosmic Theme** with deep space colors
- 💎 **Premium Glassmorphism** throughout
- 📱 **Edge-to-Edge** immersive display
- 🌓 **Beautiful Dark & Light** modes
- ✨ **Smooth Spring Animations** everywhere
- 🎭 **Floating Navigation Bar** with blur effects

</td>
<td width="50%">

### 📚 **Rich Content**

- 💬 **500+ Einstein Quotes** from his works
- 🗂️ **Multiple Categories** (Science, Life, Imagination, etc.)
- 🧪 **Scientific Context** for each quote
- 📖 **Works & Papers** section
- ⭐ **Favorites** system

</td>
</tr>
<tr>
<td width="50%">

### 🎯 **Smart Features**

- ⭐ **Favorites System** - Save your beloved quotes
- 🔔 **Daily Notifications** - Quote of the day at 9 AM
- 📤 **Share Quotes** - Spread wisdom with friends
- 💾 **Offline Access** - Works without internet
- 🎨 **Beautiful Categories** - Organized by themes

</td>
<td width="50%">

### 🤖 **AI Integration**

- 💭 **Chat with Einstein** - AI-powered conversations
- 🧠 **Intelligent Responses** - Context-aware discussions
- 💬 **Interactive Dialogue** - Discuss science & philosophy
- 📝 **Personalized Insights** - AI-enhanced quote recommendations

</td>
</tr>
</table>

## 🛠️ Tech Stack

<div align="center">

| Category | Technology |
|----------|------------|
| 🎨 **UI Framework** | Jetpack Compose (Material 3) |
| 💻 **Language** | Kotlin 2.2 |
| 🏗️ **Architecture** | MVVM + Repository Pattern |
| 💾 **Database** | Room with Coroutines & Flow |
| 🧭 **Navigation** | Compose Navigation |
| 🔄 **Async** | Kotlin Coroutines + Flow |
| 📦 **Serialization** | kotlinx.serialization |
| ⚙️ **Background** | WorkManager for notifications |
| 🎯 **DI** | Manual (ViewModel Factory) |
| 📱 **Min SDK** | API 24 (Android 7.0+) |
| 🎯 **Target SDK** | API 36 (Android 15) |

</div>

## 📱 Edge-to-Edge Implementation

This app fully implements **Android 15 Edge-to-Edge** guidelines to ensure proper handling of system navigation bars:

### ✅ What's Implemented

- **🎯 Window Insets Handling**: Bottom navigation bar automatically adjusts for system navigation buttons (3-button, 2-button, or gesture navigation)
- **🔄 Dynamic Padding**: Content padding adapts to different navigation modes and device configurations
- **🌐 Transparent System Bars**: Status bar and navigation bar are transparent for immersive experience
- **📐 Safe Area Compliance**: All interactive elements stay above system UI elements
- **🎨 Display Cutout Support**: Proper handling of notches and display cutouts

### 🔧 Technical Details

```kotlin
// MainActivity.kt - Edge-to-edge setup
WindowCompat.setDecorFitsSystemWindows(window, false)

// Bottom navigation with insets
.windowInsetsPadding(WindowInsets.navigationBars)

// Content padding to prevent overlap
.padding(bottom = if (showBottomBar) 100.dp else 0.dp)
```

### 📚 Reference
Implementation follows [Android 15 Edge-to-Edge Guidelines](https://developer.android.com/about/versions/15/behavior-changes-15#edge-to-edge)

## 📦 Project Structure

```
app/src/main/java/com/kreggscode/einsteinquotes/
├── data/
│   ├── QuoteDao.kt              # Room DAO
│   ├── QuoteDatabase.kt         # Room database
│   ├── QuoteRepository.kt       # Data repository
│   └── PreferencesManager.kt    # DataStore preferences
├── model/
│   ├── Quote.kt                 # Quote data model
│   └── Category.kt              # Category model
├── navigation/
│   └── NavGraph.kt              # Navigation graph
├── notifications/
│   ├── DailyQuoteWorker.kt      # WorkManager worker
│   └── NotificationScheduler.kt # Notification scheduler
├── ui/
│   ├── components/
│   │   ├── GlassmorphicCard.kt      # Reusable morphism cards
│   │   └── FloatingBottomBar.kt # Bottom navigation
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── ChatScreen.kt
│   │   ├── FavoritesScreen.kt
│   │   ├── WorksScreen.kt
│   │   ├── SettingsScreen.kt
│   │   ├── QuoteDetailScreen.kt
│   │   ├── CategoryQuotesScreen.kt
│   │   └── AboutScreen.kt
│   └── theme/
│       ├── Color.kt             # Cosmic-themed colors
│       ├── Theme.kt             # Theme configuration
│       └── Type.kt              # Typography
├── viewmodel/
│   ├── QuoteViewModel.kt        # Main ViewModel
│   └── ChatViewModel.kt         # Chat ViewModel
└── MainActivity.kt              # Main activity
```

## 🚀 Getting Started

### 📋 Prerequisites

```bash
✅ Android Studio Hedgehog (2023.1.1) or later
✅ JDK 17
✅ Android SDK 34
✅ Minimum Android 7.0 (API 24) device/emulator
```

### 📥 Installation

```bash
# 1. Clone the repository
git clone https://github.com/kreggscode/Einstein-Ai.git
cd Einstein-Ai

# 2. Open in Android Studio
# File → Open → Select the project folder

# 3. Sync Gradle (automatic)
# Wait for dependencies to download

# 4. Run the app
# Click the green ▶️ Run button or press Shift+F10
```

### 🎯 Quick Start

1. **First Launch**: The app will automatically load 500+ Einstein quotes into the database
2. **Browse Categories**: Explore quotes by themes like Science, Life, Imagination
3. **Save Favorites**: Tap the heart icon on any quote
4. **Enable Notifications**: Go to Settings → Toggle Daily Quotes
5. **Chat with Einstein**: Use the AI chat feature for philosophical discussions

### Dataset Files
The app uses JSON datasets located in `app/src/main/assets/`:
- `dataset1.json` - Primary quotes dataset
- `dataset2.json` - Extended quotes with bio notes

These files are automatically loaded and merged on first launch.

## 🎨 Design Philosophy

<div align="center">

### *"Imagination is more important than knowledge."*
**— Albert Einstein**

</div>

<table>
<tr>
<td width="33%" align="center">

### 🎨 **Colors**

**Light Mode**
- Clean whites
- Soft grays
- Vibrant gradients

**Dark Mode**
- Deep blacks
- Subtle contrasts
- Bright accents

</td>
<td width="33%" align="center">

### ✍️ **Typography**

- iOS-inspired fonts
- Perfect spacing
- Clear hierarchy
- Readable sizes

</td>
<td width="33%" align="center">

### 🧩 **Components**

- Glassmorphic cards
- Floating navigation
- Smooth transitions
- Premium effects

</td>
</tr>
</table>

## 🔮 Roadmap

<details>
<summary><b>🚀 Upcoming Features (Click to expand)</b></summary>

<br>

### 🎯 Version 2.0

- [ ] 🔍 **Advanced Search** - Full-text search across all quotes
- [ ] 🖼️ **Quote Images** - Generate beautiful quote cards
- [ ] 📱 **Home Widget** - Quote of the day on home screen
- [ ] 🌍 **Multi-language** - Support for Spanish, German, Italian
- [ ] 📄 **PDF Export** - Save favorites as PDF
- [ ] 🎨 **Custom Themes** - Create your own color schemes

### 🤖 AI Enhancements

- [ ] 🧠 **Smarter Chat** - Enhanced AI integration
- [ ] 💡 **Quote Recommendations** - AI-powered suggestions
- [ ] 📚 **Context Analysis** - Deeper scientific insights

### 🎵 Social Features

- [ ] 👥 **Share Collections** - Create and share quote playlists
- [ ] 🏆 **Achievements** - Unlock badges for reading quotes
- [ ] 📊 **Reading Stats** - Track your philosophical journey

</details>

## 📱 Screenshots

<div align="center">

<table>
<tr>
<td width="33%" align="center">

**🏠 Home Screen**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/1.png" alt="Home Screen" width="200"/>

*Beautiful category cards with unique gradient colors*

</td>
<td width="33%" align="center">

**💬 Chat Screen**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/2.png" alt="Chat Screen" width="200"/>

*AI-powered chat with Einstein*

</td>
<td width="33%" align="center">

**⭐ Favorites**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/3.png" alt="Favorites" width="200"/>

*Saved quotes in one place*

</td>
</tr>
<tr>
<td width="33%" align="center">

**📚 Works**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/4.png" alt="Works" width="200"/>

*Einstein's scientific works*

</td>
<td width="33%" align="center">

**⚙️ Settings**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/5.png" alt="Settings" width="200"/>

*Daily quotes, random quotes, and app preferences*

</td>
<td width="33%" align="center">

**💬 Quote Details**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/6.png" alt="Quote Details" width="200"/>

*Full quote with scientific context and actions*

</td>
</tr>
<tr>
<td width="33%" align="center">

**🎨 Category View**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/7.png" alt="Category View" width="300"/>

*Quotes filtered by category*

</td>
<td width="33%" align="center">

**📱 App Overview**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/8.png" alt="App Overview" width="300"/>

*Beautiful floating navigation and modern design*

</td>
<td width="33%" align="center">

**🧠 AI Chat**
<br><br>
<img src="https://raw.githubusercontent.com/kreggscode/Einstein-Ai/main/screenshots/9.png" alt="AI Chat" width="200"/>

*Interactive AI conversations*

</td>
</tr>
</table>

*More screenshots available in the [screenshots folder](screenshots/)*

</div>

## 🤝 Contributing

<div align="center">

### *Contributions are Welcome!*

</div>

We'd love your help making Einstein AI even better! Here's how:

1. 🍴 **Fork the repository**
2. 🌿 **Create a feature branch** (`git checkout -b feature/AmazingFeature`)
3. 💾 **Commit your changes** (`git commit -m 'Add some AmazingFeature'`)
4. 📤 **Push to the branch** (`git push origin feature/AmazingFeature`)
5. 🎉 **Open a Pull Request**

### 💡 Ways to Contribute

- 🐛 Report bugs and issues
- ✨ Suggest new features
- 📝 Improve documentation
- 🎨 Enhance UI/UX design
- 🌍 Add translations
- 🧪 Write tests

## 📄 License

<div align="center">

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

*Free to use, modify, and distribute with attribution*

</div>

## 🙏 Acknowledgments

<div align="center">

### Special Thanks

**Albert Einstein** - For centuries of timeless scientific wisdom and philosophical insights

**Open Source Community** - For amazing tools and libraries

**Jetpack Compose Team** - For revolutionizing Android UI development

---

### 📬 Contact & Support

[![GitHub](https://img.shields.io/badge/GitHub-kreggscode-181717?style=for-the-badge&logo=github)](https://github.com/kreggscode)
[![Repository](https://img.shields.io/badge/Repository-Einstein-Ai-blue?style=for-the-badge&logo=github)](https://github.com/kreggscode/Einstein-Ai)

---

<h3>⭐ Star this repo if you find it helpful!</h3>

<br>

### *"The only source of knowledge is experience."*
**— Albert Einstein**

<br>

**Made with ❤️ for science enthusiasts and beautiful design**

</div>
