# Version Update Scripts

This directory contains a comprehensive bash automation script for updating versions in the MAUI Emarsys Plugin project (macOS/Linux/WSL).

## 🚀 Quick Start (from project root)

**Just run one simple command:**
```bash
./update
```

This launches a fully interactive workflow that guides you through updating SDK and package versions with intelligent automation!

### ✨ **Smart Update Detection**
The scripts use **intelligent update detection**:
- 🔍 **Automatically checks** for latest SDK versions from GitHub
- 🎯 **Only prompts for updates** when newer versions are available  
- 📦 **Smart package versioning** with patch/minor suggestions
- 🚀 **Streamlined flow** - no unnecessary prompts when you're up to date!

**Smart Package Versioning:**
- 🔧 **'p' for patch** updates (bug fixes: 0.2.0 → 0.2.1)
- 📈 **'m' for minor** updates (new features: 0.2.0 → 0.3.0)
- ✏️ **Custom versions** still supported (e.g., 1.0.0)

**GitHub Integration:**
- 📱 **Android SDK**: https://github.com/emartech/android-emarsys-sdk/releases/latest
- 🍎 **iOS SDK**: https://github.com/emartech/ios-emarsys-sdk/releases/latest  
- 🔗 **iOS revision hashes** are automatically retrieved for the latest versions

## What Gets Updated

The scripts update the following files and versions:

1. **Package Version** (in 2 files):
   - `common/Public/GlobalUsing.cs` - Updates the `packageVersion` constant
   - `common/Public/Emarsys.Binding.nuspec` - Updates the `<version>` element

2. **Android SDK Version** (in 2 files):
   - `android/native/emarsys/build.gradle.kts` - Updates both `emarsys-sdk` and `emarsys-firebase` dependencies
   - `common/Internal/Emarsys.Binding.Internal.csproj` - Updates the `EmarsysSDKVersion` property

3. **iOS SDK Version** (in 2 files):
   - `ios/native/MauiEmarsys.xcodeproj/project.xcworkspace/xcshareddata/swiftpm/Package.resolved` - Updates the iOS Emarsys SDK version and revision hash
   - `ios/native/MauiEmarsys.xcodeproj/project.pbxproj` - Updates the minimum version requirement

## Available Scripts

### 🎯 Interactive Updater

```bash
# From project root - simple one-command approach
./update

# Or run directly from scripts folder:
./scripts/update-versions.sh
```

The script provides a complete interactive workflow that guides you through updating SDK and package versions with intelligent suggestions and automatic GitHub integration.

## 🔄 Complete Interactive Workflow

The `./update` command provides a complete workflow from version update to commit:

### **📋 Step 1: Review Changes**
- Shows `git diff` to review what files were modified
- Lets you verify the changes before proceeding

### **🧪 Step 2: Run Tests**  
- Runs `dotnet clean` on test and binding projects to ensure a fresh build
- Runs `dotnet restore test/Test.csproj` to restore dependencies
- Automatically detects and runs your test project
- Runs `dotnet test test/Test.csproj` or `dotnet test emarsys.sln`
- If tests fail, asks whether to continue or fix them first
- Ensures code quality and build integrity before committing

### **💾 Step 3: Commit Changes**
- Auto-generates detailed commit messages with version changes
- Example: `"chore: update versions\n\n- Android SDK: 3.10.2 → 3.10.3\n- Package: 0.2.0 → 0.2.1"`
- Runs `git add .` and `git commit` with confirmation

This gives you a complete, safe workflow from version update to committed changes! 🎯

## 💡 Usage Examples

**Smart Update Detection:**
```bash
$ ./update
📋 Current Versions:
   Package: 0.2.0
   Android SDK: 3.10.2
   iOS SDK: 3.9.0

🔍 Fetching latest SDK versions from GitHub...
   📱 Latest Android SDK: 3.10.3
   🍎 Latest iOS SDK: 3.9.1
   🔗 iOS revision: a1b2c3d4...

📊 Update Analysis:
   📱 Android SDK: 3.10.2 → 3.10.3 (update available!)
   🍎 iOS SDK: 3.9.0 → 3.9.1 (update available!)
   📦 Package: 0.2.0 (can always be updated)

📋 We found some SDK updates available. Let's go through them:

🤖 Step 1: Android SDK Update Available
Current: 3.10.2
Latest:  3.10.3
? Update to latest Android SDK version? (Y/n): y
   → Will update to: 3.10.3

🍎 Step 2: iOS SDK Update Available  
Current: 3.9.0
Latest:  3.9.1
? Update to latest iOS SDK version? (Y/n): y
   → Will update to: 3.9.1
   → Will use revision: a1b2c3d4...

📦 Step 3: Package Version
Package Version Update
Current: 0.2.0
Suggestions:
   🔧 Patch (bug fixes): 0.2.1
   📈 Minor (new features): 0.3.0
? Enter new version (or press Enter to keep current, 'p' for patch, 'm' for minor): m
   → Minor update: 0.3.0
```

**When SDKs are up to date:**
```bash
$ ./update
📊 Update Analysis:
   📱 Android SDK: 3.10.3 (up to date)
   🍎 iOS SDK: 3.9.1 (up to date)  
   📦 Package: 0.2.0 (can always be updated)

🎉 Great news! Your SDKs are already up to date!
We'll just check if you want to update the package version.

📦 Step 1: Package Version
Package Version Update
Current: 0.2.0
Suggestions:
   🔧 Patch (bug fixes): 0.2.1
   📈 Minor (new features): 0.3.0
? Enter new version (or press Enter to keep current, 'p' for patch, 'm' for minor): p
   → Patch update: 0.2.1
```

## 🏗️ Script Architecture

**Recent Update:** The update system has been consolidated from separate command-line and interactive scripts into a single, comprehensive interactive script that provides the best of both worlds:

- 🔍 **Auto-detects current versions** from your project files
- 🌐 **Fetches latest SDK versions** from GitHub automatically  
- 🎯 **Provides smart suggestions** for version updates
- 📋 **Shows clear summaries** of what will be changed
- 🧪 **Runs tests** to ensure compatibility
- 💾 **Handles git commits** with detailed messages

This consolidation means you only need to remember one command: `./update` - it handles everything!



## Prerequisites

### Requirements
- **bash** shell (macOS/Linux/WSL)
- **curl** for GitHub API calls
- **jq** (optional, but recommended for better JSON handling)
  ```bash
  # Install jq on macOS
  brew install jq
  
  # Install jq on Ubuntu/Debian
  sudo apt-get install jq
  ```

## 📝 Finding iOS SDK Revision Hashes

When updating iOS SDK versions, you need the corresponding Git revision hash:

1. **Visit the iOS Emarsys SDK releases:** https://github.com/emartech/ios-emarsys-sdk/tags
2. **Find your target version** (e.g., `3.11.1`)
3. **Copy the commit hash** - It's a 40-character hexadecimal string (e.g., `a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0`)
4. **Use this hash** when prompted for the iOS revision

> **💡 Tip:** The interactive script will show you the current revision hash and provide the GitHub URL for convenience.

## 📁 Project Structure

The script has been simplified and consolidated:
- `update-versions.sh` - Single comprehensive interactive script
- `update` (project root) - Simple launcher that calls the main script

### 🔄 Backup & Safety

The script creates backup files (`.bak`) during the update process and automatically cleans them up on success. If a script fails, you might find backup files that you can use to restore the original content.