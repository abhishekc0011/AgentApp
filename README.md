Field Agent Directory
A high-performance Android application designed for field agents to browse, search, and view detailed profiles of other agents with intelligent offline support and battery optimization.
🎯 Overview
This app is built for low-connectivity field operations where network conditions are unstable, battery consumption must be minimized, and agents need quick access to up-to-date agent information. The app ensures data remains available even when offline.
✨ Key Features

Fast Agent Directory - Browse through agents with smooth, jank-free scrolling
Intelligent Search - Search agents by name/email with debounced queries (300ms)
Detailed Profiles - View agent information and recent posts
Offline-First - Automatic caching with Room database ensures data availability
Pull-to-Refresh - Manual refresh to update agent list
Settings Management - Control offline mode, auto-refresh, and view network status
Background Refresh - Automatic data refresh every 15 minutes with WorkManager
Battery Optimized - Lifecycle-aware refresh pauses when app is backgrounded
Low-End Device Support - Works smoothly on older hardware with minimal memory

Installation
bash# Clone repository
git clone https://github.com/yourusername/field-agent-directory.git
cd field-agent-directory

# Sync dependencies
./gradlew sync

# Build the project
./gradlew build

# Install on device/emulator
./gradlew installDebug

# Or run directly in Android Studio
# Click the Run button (Shift + F10)
Configuration
No API keys or configuration needed! The app uses the public DummyJSON API:

Base URL: https://dummyjson.com/
Endpoints:

/users - Get agents list (paginated)
/users/{id} - Get single agent
/posts/user/{id} - Get agent's posts
/users/search?q=query - Search agents
