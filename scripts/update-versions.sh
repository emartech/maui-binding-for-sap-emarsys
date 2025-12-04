#!/bin/bash

# Interactive Version Updater for MAUI Emarsys Plugin
# This script walks you through updating versions step by step

set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
BLUE='\033[0;34m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# Get script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

# File paths (relative to project root)
VERSION_FILE_PATH="$PROJECT_ROOT/VERSION"
ANDROID_GRADLE_PATH="$PROJECT_ROOT/android/native/emarsys/build.gradle.kts"
ANDROID_BINDING_INTERNAL_PATH="$PROJECT_ROOT/common/Internal/Emarsys.Binding.Internal.csproj"
IOS_PACKAGE_RESOLVED_PATH="$PROJECT_ROOT/ios/native/MauiEmarsys.xcodeproj/project.xcworkspace/xcshareddata/swiftpm/Package.resolved"
IOS_PROJECT_PBXPROJ_PATH="$PROJECT_ROOT/ios/native/MauiEmarsys.xcodeproj/project.pbxproj"
CHANGELOG_PATH="$PROJECT_ROOT/CHANGELOG.md"

# Function to get current versions
get_current_versions() {
    echo -e "${CYAN}📋 Current Versions:${NC}"
    
    # Get current package version from VERSION file
    if [ -f "$VERSION_FILE_PATH" ]; then
        CURRENT_PACKAGE=$(cat "$VERSION_FILE_PATH" | tr -d '\n\r' | xargs)
    else
        echo -e "${RED}❌ Error: VERSION file not found at $VERSION_FILE_PATH${NC}"
        exit 1
    fi
    
    echo -e "${WHITE}   Package: ${YELLOW}$CURRENT_PACKAGE${NC}"
    
    # Get current Android SDK version
    if [ -f "$ANDROID_GRADLE_PATH" ]; then
        CURRENT_ANDROID=$(grep -o 'emarsys-sdk:[^"]*' "$ANDROID_GRADLE_PATH" | head -1 | cut -d: -f2)
        echo -e "${WHITE}   Android SDK: ${YELLOW}$CURRENT_ANDROID${NC}"
    fi
    
    # Get current iOS SDK version and revision
    if [ -f "$IOS_PACKAGE_RESOLVED_PATH" ]; then
        if command -v jq >/dev/null 2>&1; then
            CURRENT_IOS=$(jq -r '.pins[] | select(.identity == "ios-emarsys-sdk") | .state.version' "$IOS_PACKAGE_RESOLVED_PATH" 2>/dev/null || echo "")
            CURRENT_IOS_REVISION=$(jq -r '.pins[] | select(.identity == "ios-emarsys-sdk") | .state.revision' "$IOS_PACKAGE_RESOLVED_PATH" 2>/dev/null || echo "")
        else
            CURRENT_IOS=$(grep -A5 -B2 'ios-emarsys-sdk' "$IOS_PACKAGE_RESOLVED_PATH" | grep '"version"' | sed 's/.*"version" : "\([^"]*\)".*/\1/')
            CURRENT_IOS_REVISION=$(grep -A5 -B2 'ios-emarsys-sdk' "$IOS_PACKAGE_RESOLVED_PATH" | grep '"revision"' | sed 's/.*"revision" : "\([^"]*\)".*/\1/')
        fi
        echo -e "${WHITE}   iOS SDK: ${YELLOW}$CURRENT_IOS${NC} (revision: ${CYAN}${CURRENT_IOS_REVISION:0:8}...${NC})"
    fi
    echo ""
}

# Function to fetch latest SDK versions from GitHub
fetch_latest_sdk_versions() {
    echo -e "${CYAN}🔍 Fetching latest SDK versions from GitHub...${NC}"
    
    # Fetch latest Android SDK version
    if command -v curl >/dev/null 2>&1; then
        LATEST_ANDROID=$(curl -s "https://api.github.com/repos/emartech/android-emarsys-sdk/releases/latest" 2>/dev/null | grep '"tag_name"' | sed 's/.*"tag_name": "\([^"]*\)".*/\1/' || echo "")
        if [ -n "$LATEST_ANDROID" ]; then
            echo -e "${WHITE}   📱 Latest Android SDK: ${GREEN}$LATEST_ANDROID${NC}"
        else
            echo -e "${YELLOW}   ⚠️  Could not fetch latest Android SDK version${NC}"
        fi
        
        # Fetch latest iOS SDK version and revision
        LATEST_IOS=$(curl -s "https://api.github.com/repos/emartech/ios-emarsys-sdk/releases/latest" 2>/dev/null | grep '"tag_name"' | sed 's/.*"tag_name": "\([^"]*\)".*/\1/' || echo "")
        if [ -n "$LATEST_IOS" ]; then
            echo -e "${WHITE}   🍎 Latest iOS SDK: ${GREEN}$LATEST_IOS${NC}"
            
            # Get the tag reference first
            TAG_SHA=$(curl -s "https://api.github.com/repos/emartech/ios-emarsys-sdk/git/refs/tags/$LATEST_IOS" 2>/dev/null | grep '"sha"' | sed 's/.*"sha": "\([^"]*\)".*/\1/' || echo "")
            if [ -n "$TAG_SHA" ]; then
                # Check if this is a tag object or direct commit
                TAG_TYPE=$(curl -s "https://api.github.com/repos/emartech/ios-emarsys-sdk/git/tags/$TAG_SHA" 2>/dev/null | grep '"object"' -A 3 | grep '"type"' | sed 's/.*"type": "\([^"]*\)".*/\1/' || echo "")
                if [ "$TAG_TYPE" = "commit" ]; then
                    # Tag points directly to commit, get the commit SHA
                    LATEST_IOS_REVISION=$(curl -s "https://api.github.com/repos/emartech/ios-emarsys-sdk/git/tags/$TAG_SHA" 2>/dev/null | grep '"object"' -A 3 | grep '"sha"' | sed 's/.*"sha": "\([^"]*\)".*/\1/' || echo "")
                else
                    # Tag object itself, use as revision
                    LATEST_IOS_REVISION="$TAG_SHA"
                fi
                
                if [ -n "$LATEST_IOS_REVISION" ]; then
                    echo -e "${WHITE}   🔗 iOS revision: ${GREEN}${LATEST_IOS_REVISION:0:8}...${NC}"
                fi
            fi
        else
            echo -e "${YELLOW}   ⚠️  Could not fetch latest iOS SDK version${NC}"
        fi
    else
        echo -e "${YELLOW}   ⚠️  curl not available - cannot fetch latest versions${NC}"
        LATEST_ANDROID=""
        LATEST_IOS=""
        LATEST_IOS_REVISION=""
    fi
    echo ""
}

# Function to calculate next package version suggestions
calculate_package_versions() {
    local current_version="$1"
    
    # Parse version (e.g., "0.2.0" → major=0, minor=2, patch=0)
    if [[ "$current_version" =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
        local major="${BASH_REMATCH[1]}"
        local minor="${BASH_REMATCH[2]}"
        local patch="${BASH_REMATCH[3]}"
        
        SUGGESTED_PATCH="$major.$minor.$((patch + 1))"
        SUGGESTED_MINOR="$major.$((minor + 1)).0"
    else
        SUGGESTED_PATCH=""
        SUGGESTED_MINOR=""
    fi
}

# Function to prompt for version with validation
prompt_for_version() {
    local prompt_text="$1"
    local current_version="$2"
    local example="$3"
    local variable_name="$4"
    local latest_version="$5"  # Optional latest version from GitHub
    
    echo -e "${BLUE}${BOLD}$prompt_text${NC}"
    echo -e "${WHITE}Current: ${YELLOW}$current_version${NC}"
    if [ -n "$latest_version" ] && [ "$latest_version" != "$current_version" ]; then
        echo -e "${WHITE}Latest:  ${GREEN}$latest_version${NC} ${CYAN}(from GitHub)${NC}"
    fi
    echo -e "${WHITE}Example: ${CYAN}$example${NC}"
    
    local prompt_msg="? Enter new version (or press Enter to keep current"
    if [ -n "$latest_version" ] && [ "$latest_version" != "$current_version" ]; then
        prompt_msg="$prompt_msg, 'l' for latest"
    fi
    prompt_msg="$prompt_msg): "
    
    while true; do
        read -p $'\033[1;32m'"$prompt_msg"$'\033[0m' input
        
        if [ -z "$input" ]; then
            # Keep current version
            eval "$variable_name='$current_version'"
            echo -e "${WHITE}   → Keeping: ${YELLOW}$current_version${NC}"
            break
        elif [ "$input" = "l" ] && [ -n "$latest_version" ]; then
            # Use latest version from GitHub
            eval "$variable_name='$latest_version'"
            echo -e "${WHITE}   → Using latest: ${GREEN}$latest_version${NC}"
            break
        elif [[ "$input" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
            # Valid version format
            eval "$variable_name='$input'"
            echo -e "${WHITE}   → New version: ${GREEN}$input${NC}"
            break
        else
            echo -e "${RED}   ✗ Invalid format. Please use semantic versioning (e.g., $example)${NC}"
        fi
    done
    echo ""
}

# Function to prompt for package version with smart suggestions
prompt_for_package_version() {
    local current_version="$1"
    local variable_name="$2"
    
    # Calculate smart version suggestions
    calculate_package_versions "$current_version"
    
    echo -e "${BLUE}${BOLD}Package Version Update${NC}"
    echo -e "${WHITE}Current: ${YELLOW}$current_version${NC}"
    
    if [ -n "$SUGGESTED_PATCH" ] && [ -n "$SUGGESTED_MINOR" ]; then
        echo -e "${WHITE}Suggestions:${NC}"
        echo -e "${WHITE}   🔧 Patch (bug fixes): ${GREEN}$SUGGESTED_PATCH${NC}"
        echo -e "${WHITE}   📈 Minor (new features): ${GREEN}$SUGGESTED_MINOR${NC}"
    fi
    
    echo -e "${WHITE}Example: ${CYAN}0.2.0${NC}"
    echo ""
    
    local prompt_msg="? Enter new version (or press Enter to keep current"
    if [ -n "$SUGGESTED_PATCH" ]; then
        prompt_msg="$prompt_msg, 'p' for patch, 'm' for minor"
    fi
    prompt_msg="$prompt_msg): "
    
    while true; do
        read -p $'\033[1;32m'"$prompt_msg"$'\033[0m' input
        
        if [ -z "$input" ]; then
            # Keep current version
            eval "$variable_name='$current_version'"
            echo -e "${WHITE}   → Keeping: ${YELLOW}$current_version${NC}"
            break
        elif [ "$input" = "p" ] && [ -n "$SUGGESTED_PATCH" ]; then
            # Use patch version
            eval "$variable_name='$SUGGESTED_PATCH'"
            echo -e "${WHITE}   → Patch update: ${GREEN}$SUGGESTED_PATCH${NC}"
            break
        elif [ "$input" = "m" ] && [ -n "$SUGGESTED_MINOR" ]; then
            # Use minor version
            eval "$variable_name='$SUGGESTED_MINOR'"
            echo -e "${WHITE}   → Minor update: ${GREEN}$SUGGESTED_MINOR${NC}"
            break
        elif [[ "$input" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
            # Valid version format
            eval "$variable_name='$input'"
            echo -e "${WHITE}   → New version: ${GREEN}$input${NC}"
            break
        else
            echo -e "${RED}   ✗ Invalid format. Please use semantic versioning (e.g., 0.2.0)${NC}"
        fi
    done
    echo ""
}

# Function to get the latest released version from changelog
get_latest_changelog_version() {
    if [ -f "$CHANGELOG_PATH" ] && [ -s "$CHANGELOG_PATH" ]; then
        # Look for version patterns in changelog (e.g., "# 1.0.0")
        grep -E "^#\s+[0-9]+\.[0-9]+\.[0-9]+" "$CHANGELOG_PATH" | head -1 | grep -oE "[0-9]+\.[0-9]+\.[0-9]+" || echo ""
    else
        echo ""
    fi
}

# Function to check if changelog needs updating
needs_changelog_update() {
    local new_package_version="$1"
    local current_package_version="$2"
    local current_android="$3"
    local new_android="$4"
    local current_ios="$5"
    local new_ios="$6"
    
    # Need update if package version changed OR any SDK version changed
    if [ "$new_package_version" != "$current_package_version" ] || 
       [ "$current_android" != "$new_android" ] || 
       [ "$current_ios" != "$new_ios" ]; then
        echo "true"
    else
        echo "false"
    fi
}

# Function to generate changelog entry
generate_changelog_entry() {
    local new_package_version="$1"
    local current_package="$2"
    local current_android="$3"
    local new_android="$4"
    local current_ios="$5"
    local new_ios="$6"
    
    local entry=""
    
    # Version header
    entry="# $new_package_version\n"
    
    # SDK Updates section (if any)
    if [ "$current_android" != "$new_android" ] || [ "$current_ios" != "$new_ios" ]; then
        entry="${entry}## What's changed\n"
        
        if [ "$current_android" != "$new_android" ]; then
            entry="${entry}* Updated underlying [Android Emarsys SDK](https://github.com/emartech/android-emarsys-sdk/releases/tag/$new_android) to $new_android\n"
        fi
        
        if [ "$current_ios" != "$new_ios" ]; then
            entry="${entry}* Updated underlying [iOS Emarsys SDK](https://github.com/emartech/ios-emarsys-sdk/releases/tag/$new_ios) to $new_ios\n"
        fi
        
        entry="${entry}\n"
    fi
    
    # Placeholder sections for manual additions
    entry="${entry}<!-- Uncomment and fill in the sections you need:\n\n"
    entry="${entry}## What's new\n"
    entry="${entry}* Description of new features\n\n"
    entry="${entry}## What's fixed\n"
    entry="${entry}* Description of bug fixes\n\n"
    entry="${entry}## What's changed\n"
    entry="${entry}* Description of changes or improvements\n\n"
    entry="${entry}-->\n\n"
    
    echo -e "$entry"
}

# Function to update changelog
update_changelog() {
    local new_package_version="$1"
    local current_package="$2"
    local current_android="$3"
    local new_android="$4"
    local current_ios="$5"
    local new_ios="$6"
    
    local changelog_entry=$(generate_changelog_entry "$new_package_version" "$current_package" "$current_android" "$new_android" "$current_ios" "$new_ios")
    
    # Always replace the entire changelog with just the new entry
    # This removes all previous versions and keeps only the current one
    echo -e "$changelog_entry" > "$CHANGELOG_PATH"
}

# Function to prompt for changelog update
prompt_for_changelog_update() {
    local new_package_version="$1"
    local current_package="$2"
    local current_android="$3"
    local new_android="$4"
    local current_ios="$5"
    local new_ios="$6"
    
    echo -e "${BLUE}${BOLD}📝 CHANGELOG.md Update${NC}"
    echo -e "${WHITE}A new package version requires updating the changelog.${NC}"
    echo ""
    
    # Show preview of what will be added
    echo -e "${WHITE}Preview of changelog entry:${NC}"
    echo -e "${CYAN}----------------------------------------${NC}"
    generate_changelog_entry "$new_package_version" "$current_package" "$current_android" "$new_android" "$current_ios" "$new_ios"
    echo -e "${CYAN}----------------------------------------${NC}"
    echo ""
    
    echo -e "${WHITE}This entry includes SDK updates and package version change.${NC}"
    echo -e "${WHITE}After the update, you can manually add any additional changes.${NC}"
    echo ""
    
    while true; do
        read -p $'\033[1;32m? Update CHANGELOG.md with this entry? (Y/n): \033[0m' changelog_update
        changelog_update=${changelog_update:-y}  # Default to yes
        
        if [[ "$changelog_update" =~ ^[Yy]$ ]]; then
            return 0  # Yes, update changelog
        elif [[ "$changelog_update" =~ ^[Nn]$ ]]; then
            echo -e "${YELLOW}   → Skipping changelog update${NC}"
            return 1  # No, skip changelog update
        else
            echo -e "${RED}   ✗ Please answer 'y' for yes or 'n' for no${NC}"
        fi
    done
}

# Function to check git status
check_git_status() {
    echo -e "${CYAN}🔍 Checking git status...${NC}"
    
    # Check if we're in a git repository
    if ! git rev-parse --git-dir > /dev/null 2>&1; then
        echo -e "${RED}❌ Not in a git repository. Please run this script from within a git repository.${NC}"
        exit 1
    fi
    
    # Check for uncommitted changes
    if ! git diff --quiet || ! git diff --cached --quiet; then
        echo -e "${RED}❌ You have uncommitted changes in your repository.${NC}"
        echo -e "${WHITE}Please commit or stash your changes before running the version updater.${NC}"
        echo ""
        
        echo -e "${YELLOW}Uncommitted changes:${NC}"
        git status --porcelain
        echo ""
        
        echo -e "${WHITE}To proceed, first handle your changes:${NC}"
        echo -e "${CYAN}   git add . && git commit -m \"your message\"${NC}"
        echo -e "${CYAN}   git stash${NC}"
        echo -e "${CYAN}   git reset --hard HEAD${NC} ${YELLOW}(careful - discards changes!)${NC}"
        
        exit 1
    else
        echo -e "${GREEN}✅ Git status is clean - ready to proceed!${NC}"
        echo ""
    fi
}

# Main script
echo -e "${BOLD}${GREEN}🚀 MAUI Emarsys Plugin - Version Updater${NC}"
echo -e "${WHITE}This tool will guide you through updating SDK and package versions.${NC}"
echo ""

# Check git status first
check_git_status

# Show current versions
get_current_versions

# Fetch latest SDK versions from GitHub
fetch_latest_sdk_versions

# Check for available updates
ANDROID_UPDATE_AVAILABLE=false
IOS_UPDATE_AVAILABLE=false

if [ -n "$LATEST_ANDROID" ] && [ "$CURRENT_ANDROID" != "$LATEST_ANDROID" ]; then
    ANDROID_UPDATE_AVAILABLE=true
fi

if [ -n "$LATEST_IOS" ] && [ "$CURRENT_IOS" != "$LATEST_IOS" ]; then
    IOS_UPDATE_AVAILABLE=true
fi

echo -e "${CYAN}📊 Update Analysis:${NC}"
if [ "$ANDROID_UPDATE_AVAILABLE" = true ]; then
    echo -e "${WHITE}   📱 Android SDK: ${YELLOW}$CURRENT_ANDROID${NC} → ${GREEN}$LATEST_ANDROID${NC} ${CYAN}(update available!)${NC}"
else
    echo -e "${WHITE}   📱 Android SDK: ${GREEN}$CURRENT_ANDROID${NC} ${CYAN}(up to date)${NC}"
fi

if [ "$IOS_UPDATE_AVAILABLE" = true ]; then
    echo -e "${WHITE}   🍎 iOS SDK: ${YELLOW}$CURRENT_IOS${NC} → ${GREEN}$LATEST_IOS${NC} ${CYAN}(update available!)${NC}"
else
    echo -e "${WHITE}   🍎 iOS SDK: ${GREEN}$CURRENT_IOS${NC} ${CYAN}(up to date)${NC}"
fi

echo -e "${WHITE}   📦 Package: ${YELLOW}$CURRENT_PACKAGE${NC} ${CYAN}(can always be updated)${NC}"
echo ""

# Show what we'll be doing
if [ "$ANDROID_UPDATE_AVAILABLE" = false ] && [ "$IOS_UPDATE_AVAILABLE" = false ]; then
    echo -e "${GREEN}🎉 Great news! Your SDKs are already up to date!${NC}"
    echo -e "${WHITE}We'll just check if you want to update the package version.${NC}"
    echo ""
else
    echo -e "${CYAN}📋 We found some SDK updates available. Let's go through them:${NC}"
    echo ""
fi

# Initialize versions with current values
NEW_ANDROID_VERSION="$CURRENT_ANDROID"
NEW_IOS_VERSION="$CURRENT_IOS"
NEW_IOS_REVISION="$CURRENT_IOS_REVISION"

STEP_COUNT=1

# Step 1: Android SDK Update (only if update available)
if [ "$ANDROID_UPDATE_AVAILABLE" = true ]; then
    echo -e "${BLUE}${BOLD}🤖 Step $STEP_COUNT: Android SDK Update Available${NC}"
    echo -e "${WHITE}Current: ${YELLOW}$CURRENT_ANDROID${NC}"
    echo -e "${WHITE}Latest:  ${GREEN}$LATEST_ANDROID${NC}"
    echo ""
    
    while true; do
        read -p $'\033[1;32m? Update to latest Android SDK version? (Y/n): \033[0m' android_update
        android_update=${android_update:-y}  # Default to yes
        
        if [[ "$android_update" =~ ^[Yy]$ ]]; then
            NEW_ANDROID_VERSION="$LATEST_ANDROID"
            echo -e "${WHITE}   → Will update to: ${GREEN}$LATEST_ANDROID${NC}"
            break
        elif [[ "$android_update" =~ ^[Nn]$ ]]; then
            echo -e "${WHITE}   → Keeping current: ${YELLOW}$CURRENT_ANDROID${NC}"
            break
        else
            echo -e "${RED}   ✗ Please answer 'y' for yes or 'n' for no${NC}"
        fi
    done
    echo ""
    STEP_COUNT=$((STEP_COUNT + 1))
fi

# Step 2: iOS SDK Update (only if update available)
if [ "$IOS_UPDATE_AVAILABLE" = true ]; then
    echo -e "${BLUE}${BOLD}🍎 Step $STEP_COUNT: iOS SDK Update Available${NC}"
    echo -e "${WHITE}Current: ${YELLOW}$CURRENT_IOS${NC}"
    echo -e "${WHITE}Latest:  ${GREEN}$LATEST_IOS${NC}"
    echo ""
    
    while true; do
        read -p $'\033[1;32m? Update to latest iOS SDK version? (Y/n): \033[0m' ios_update
        ios_update=${ios_update:-y}  # Default to yes
        
        if [[ "$ios_update" =~ ^[Yy]$ ]]; then
            NEW_IOS_VERSION="$LATEST_IOS"
            NEW_IOS_REVISION="$LATEST_IOS_REVISION"
            echo -e "${WHITE}   → Will update to: ${GREEN}$LATEST_IOS${NC}"
            echo -e "${WHITE}   → Will use revision: ${GREEN}${LATEST_IOS_REVISION:0:8}...${NC}"
            break
        elif [[ "$ios_update" =~ ^[Nn]$ ]]; then
            echo -e "${WHITE}   → Keeping current: ${YELLOW}$CURRENT_IOS${NC}"
            break
        else
            echo -e "${RED}   ✗ Please answer 'y' for yes or 'n' for no${NC}"
        fi
    done
    echo ""
    STEP_COUNT=$((STEP_COUNT + 1))
fi

# Step N: Package Version (always ask)
echo -e "${BLUE}${BOLD}📦 Step $STEP_COUNT: Package Version${NC}"
prompt_for_package_version "$CURRENT_PACKAGE" "NEW_PACKAGE_VERSION"
STEP_COUNT=$((STEP_COUNT + 1))

# Step N+1: CHANGELOG.md Update (if package version changed OR SDK versions changed)
CHANGELOG_UPDATE_NEEDED=$(needs_changelog_update "$NEW_PACKAGE_VERSION" "$CURRENT_PACKAGE" "$CURRENT_ANDROID" "$NEW_ANDROID_VERSION" "$CURRENT_IOS" "$NEW_IOS_VERSION")
UPDATE_CHANGELOG=false

if [ "$CHANGELOG_UPDATE_NEEDED" = "true" ]; then
    echo -e "${BLUE}${BOLD}📝 Step $STEP_COUNT: CHANGELOG.md Update${NC}"
    
    # Create backup of current changelog for potential rollback
    CHANGELOG_BACKUP=""
    if [ -f "$CHANGELOG_PATH" ]; then
        CHANGELOG_BACKUP=$(cat "$CHANGELOG_PATH")
        CHANGELOG_HASH_BEFORE=$(shasum -a 256 "$CHANGELOG_PATH" | cut -d' ' -f1)
    fi
    
    # First, auto-update the changelog with SDK updates
    echo -e "${WHITE}Updating CHANGELOG.md with SDK and version changes...${NC}"
    if update_changelog "$NEW_PACKAGE_VERSION" "$CURRENT_PACKAGE" "$CURRENT_ANDROID" "$NEW_ANDROID_VERSION" "$CURRENT_IOS" "$NEW_IOS_VERSION"; then
        echo -e "${GREEN}✓ CHANGELOG.md updated with automatic entries${NC}"
        UPDATE_CHANGELOG=true
    else
        echo -e "${RED}✗ Failed to update CHANGELOG.md${NC}"
        exit 1
    fi
    
    echo ""
    echo -e "${YELLOW}${BOLD}📝 IMPORTANT: Manual Changelog Review Required${NC}"
    echo -e "${WHITE}The CHANGELOG.md has been updated with SDK updates and/or version changes.${NC}"
    echo -e "${WHITE}You MUST now complete the changelog by:${NC}"
    echo -e "${CYAN}   1. REMOVING the template comment block (<!-- Uncomment and fill... -->)${NC}"
    echo -e "${CYAN}   2. Adding actual sections for your changes:${NC}"
    echo -e "${CYAN}      • ## What's new (for new features)${NC}"
    echo -e "${CYAN}      • ## What's fixed (for bug fixes)${NC}"
    echo -e "${CYAN}      • ## What's changed (for breaking changes, improvements)${NC}"
    echo -e "${CYAN}   3. Filling in real content with actual changes made${NC}"
    echo ""
    echo -e "${WHITE}The script will verify that template comments are removed and will only proceed${NC}"
    echo -e "${WHITE}if you complete the changelog with actual content.${NC}"
    echo ""
    
    # Open changelog for editing
    if command -v code >/dev/null 2>&1; then
        echo -e "${CYAN}Opening CHANGELOG.md in VS Code...${NC}"
        code "$CHANGELOG_PATH"
    elif command -v open >/dev/null 2>&1; then
        echo -e "${CYAN}Opening CHANGELOG.md with default editor...${NC}"
        open "$CHANGELOG_PATH"
    else
        echo -e "${YELLOW}Please edit: $CHANGELOG_PATH${NC}"
    fi
    
    echo ""
    echo -e "${WHITE}Press any key when you have finished editing the changelog...${NC}"
    read -n 1 -s
    echo ""
    
    # Check if changelog was actually modified and template comments removed
    CHANGELOG_HASH_AFTER=""
    if [ -f "$CHANGELOG_PATH" ]; then
        CHANGELOG_HASH_AFTER=$(shasum -a 256 "$CHANGELOG_PATH" | cut -d' ' -f1)
    fi
    
    # Check if template comments are still present
    if grep -q "<!-- Uncomment and fill in the sections you need:" "$CHANGELOG_PATH" 2>/dev/null; then
        echo -e "${RED}❌ Template comments still present in CHANGELOG.md${NC}"
        echo -e "${WHITE}You must remove the template comments and add actual changelog content.${NC}"
        echo -e "${WHITE}Please:${NC}"
        echo -e "${CYAN}   1. Remove the <!-- Uncomment and fill... --> comment block${NC}"
        echo -e "${CYAN}   2. Add actual sections for your changes (What's new, What's fixed, etc.)${NC}"
        echo -e "${CYAN}   3. Fill in real content instead of template placeholders${NC}"
        echo ""
        
        # Restore original changelog
        echo -e "${YELLOW}🔄 Restoring original CHANGELOG.md and discarding all changes...${NC}"
        if [ -n "$CHANGELOG_BACKUP" ]; then
            echo "$CHANGELOG_BACKUP" > "$CHANGELOG_PATH"
            echo -e "${GREEN}✓ CHANGELOG.md restored to original state${NC}"
            echo -e "${WHITE}   All automatic changes have been discarded${NC}"
        fi
        
        echo ""
        echo -e "${YELLOW}Update cancelled. No files have been modified.${NC}"
        echo -e "${WHITE}Please complete the changelog properly and run the script again.${NC}"
        exit 1
    elif [ "$CHANGELOG_HASH_BEFORE" = "$CHANGELOG_HASH_AFTER" ]; then
        echo -e "${RED}❌ No changes detected in CHANGELOG.md${NC}"
        echo -e "${WHITE}You must update the changelog with your changes before proceeding.${NC}"
        echo -e "${WHITE}Please add details about bug fixes, features, or other changes.${NC}"
        echo ""
        
        # Restore original changelog
        echo -e "${YELLOW}🔄 Restoring original CHANGELOG.md and discarding all changes...${NC}"
        if [ -n "$CHANGELOG_BACKUP" ]; then
            echo "$CHANGELOG_BACKUP" > "$CHANGELOG_PATH"
            echo -e "${GREEN}✓ CHANGELOG.md restored to original state${NC}"
            echo -e "${WHITE}   All automatic changes have been discarded${NC}"
        fi
        
        echo ""
        echo -e "${YELLOW}Update cancelled. No files have been modified.${NC}"
        echo -e "${WHITE}Please run the script again after considering what changes to document.${NC}"
        exit 1
    else
        echo -e "${GREEN}✓ Changelog properly completed - proceeding with update${NC}"
    fi
    echo ""
fi

# Summary of changes
echo -e "${CYAN}${BOLD}📋 Summary of Changes:${NC}"
echo -e "${WHITE}   Android SDK: ${YELLOW}$CURRENT_ANDROID${NC} → ${GREEN}$NEW_ANDROID_VERSION${NC}"
echo -e "${WHITE}   iOS SDK:     ${YELLOW}$CURRENT_IOS${NC} → ${GREEN}$NEW_IOS_VERSION${NC}"
if [ "$CURRENT_IOS" != "$NEW_IOS_VERSION" ]; then
    echo -e "${WHITE}   iOS Rev:     ${CYAN}${CURRENT_IOS_REVISION:0:8}...${NC} → ${GREEN}${NEW_IOS_REVISION:0:8}...${NC}"
fi
echo -e "${WHITE}   Package:     ${YELLOW}$CURRENT_PACKAGE${NC} → ${GREEN}$NEW_PACKAGE_VERSION${NC}"
if [ "$UPDATE_CHANGELOG" = true ]; then
    echo -e "${WHITE}   Changelog:   ${GREEN}Updated and manually reviewed${NC}"
fi
echo ""

# Check if any changes were made
CHANGES_MADE=false
if [ "$CURRENT_PACKAGE" != "$NEW_PACKAGE_VERSION" ]; then CHANGES_MADE=true; fi
if [ "$CURRENT_ANDROID" != "$NEW_ANDROID_VERSION" ]; then CHANGES_MADE=true; fi
if [ "$CURRENT_IOS" != "$NEW_IOS_VERSION" ]; then CHANGES_MADE=true; fi
if [ "$UPDATE_CHANGELOG" = true ]; then CHANGES_MADE=true; fi

if [ "$CHANGES_MADE" = false ]; then
    echo -e "${YELLOW}ℹ️  No changes detected. All versions remain the same.${NC}"
    exit 0
fi

# Final confirmation
echo -e "${BOLD}${BLUE}🤔 Do you want to apply these changes?${NC}"
read -p $'\033[1;32m? Proceed with update? (y/N): \033[0m' confirm

if [[ ! "$confirm" =~ ^[yY]$ ]]; then
    echo -e "${YELLOW}❌ Update cancelled.${NC}"
    exit 0
fi

echo ""
echo -e "${GREEN}🔄 Applying updates...${NC}"

# Apply updates
UPDATE_SUCCESS=true

# Update VERSION file - Package version
if [ "$CURRENT_PACKAGE" != "$NEW_PACKAGE_VERSION" ]; then
    echo -ne "${WHITE}   📝 Updating VERSION file... ${NC}"
    if echo "$NEW_PACKAGE_VERSION" > "$VERSION_FILE_PATH" 2>/dev/null; then
        echo -e "${GREEN}✓${NC}"
        echo -e "${WHITE}      (Version will be automatically injected into GlobalUsing.cs and .nuspec at build time)${NC}"
    else
        echo -e "${RED}✗${NC}"
        UPDATE_SUCCESS=false
    fi
fi

# Update Android build.gradle.kts - Android SDK version
if [ "$CURRENT_ANDROID" != "$NEW_ANDROID_VERSION" ]; then
    echo -ne "${WHITE}   📝 Updating Android build.gradle.kts... ${NC}"
    if sed -i "" \
        -e "s/implementation(\"com\.emarsys:emarsys-sdk:[^\"]*\")/implementation(\"com.emarsys:emarsys-sdk:$NEW_ANDROID_VERSION\")/" \
        -e "s/implementation(\"com\.emarsys:emarsys-firebase:[^\"]*\")/implementation(\"com.emarsys:emarsys-firebase:$NEW_ANDROID_VERSION\")/" \
        -e "s/\"copyDependencies\"(\"com\.emarsys:emarsys-sdk:[^\"]*\")/\"copyDependencies\"(\"com.emarsys:emarsys-sdk:$NEW_ANDROID_VERSION\")/" \
        -e "s/\"copyDependencies\"(\"com\.emarsys:emarsys-firebase:[^\"]*\")/\"copyDependencies\"(\"com.emarsys:emarsys-firebase:$NEW_ANDROID_VERSION\")/" \
        "$ANDROID_GRADLE_PATH" 2>/dev/null; then
        echo -e "${GREEN}✓${NC}"
    else
        echo -e "${RED}✗${NC}"
        UPDATE_SUCCESS=false
    fi
    
    # Also update Android Binding Internal project - SDK version
    echo -ne "${WHITE}   📝 Updating Emarsys.Binding.Internal.csproj... ${NC}"
    if sed -i "" "s/<EmarsysSDKVersion>[^<]*<\/EmarsysSDKVersion>/<EmarsysSDKVersion>$NEW_ANDROID_VERSION<\/EmarsysSDKVersion>/" "$ANDROID_BINDING_INTERNAL_PATH" 2>/dev/null; then
        echo -e "${GREEN}✓${NC}"
    else
        echo -e "${RED}✗${NC}"
        UPDATE_SUCCESS=false
    fi
fi

# Update iOS Package.resolved - iOS SDK version and revision
if [ "$CURRENT_IOS" != "$NEW_IOS_VERSION" ]; then
    echo -ne "${WHITE}   📝 Updating iOS Package.resolved... ${NC}"
    if command -v jq >/dev/null 2>&1; then
        # Use jq if available for better JSON handling
        if jq --arg version "$NEW_IOS_VERSION" --arg revision "$NEW_IOS_REVISION" '.pins[] |= if .identity == "ios-emarsys-sdk" then (.state.version = $version | .state.revision = $revision) else . end' "$IOS_PACKAGE_RESOLVED_PATH" > "$IOS_PACKAGE_RESOLVED_PATH.tmp"; then
            mv "$IOS_PACKAGE_RESOLVED_PATH.tmp" "$IOS_PACKAGE_RESOLVED_PATH"
            echo -e "${GREEN}✓${NC}"
        else
            rm -f "$IOS_PACKAGE_RESOLVED_PATH.tmp"
            echo -e "${RED}✗${NC}"
            UPDATE_SUCCESS=false
        fi
    else
        # Fallback to sed for version and revision replacement
        if sed -i "" -e "s/\"revision\" : \"[^\"]*\"/\"revision\" : \"$NEW_IOS_REVISION\"/" -e "s/\"version\" : \"[^\"]*\"/\"version\" : \"$NEW_IOS_VERSION\"/" "$IOS_PACKAGE_RESOLVED_PATH" 2>/dev/null; then
            echo -e "${GREEN}✓${NC}"
        else
            echo -e "${RED}✗${NC}"
            UPDATE_SUCCESS=false
        fi
    fi
    
    # Also update iOS project.pbxproj - minimum version requirement
    echo -ne "${WHITE}   📝 Updating iOS project.pbxproj... ${NC}"
    if sed -i "" "s/minimumVersion = [0-9]*\.[0-9]*\.[0-9]*;/minimumVersion = $NEW_IOS_VERSION;/" "$IOS_PROJECT_PBXPROJ_PATH" 2>/dev/null; then
        echo -e "${GREEN}✓${NC}"
    else
        echo -e "${RED}✗${NC}"
        UPDATE_SUCCESS=false
    fi
fi

echo ""

if [ "$UPDATE_SUCCESS" = true ]; then
    echo -e "${GREEN}${BOLD}🎉 Version update completed successfully!${NC}"
    echo ""
    
    # Interactive next steps
    echo -e "${CYAN}${BOLD}📝 Next Steps - Let's go through them together:${NC}"
    echo ""
    
    # Step 1: Review changes
    echo -e "${BLUE}${BOLD}📋 Step 1: Review Changes${NC}"
    echo -e "${WHITE}Would you like to see what files were changed?${NC}"
    read -p $'\033[1;32m? Run git diff to review changes? (Y/n): \033[0m' review_confirm
    
    if [[ ! "$review_confirm" =~ ^[nN]$ ]]; then
        echo ""
        echo -e "${CYAN}Running: git diff${NC}"
        (cd "$PROJECT_ROOT" && git diff)
        echo ""
        echo -e "${WHITE}Press any key to continue...${NC}"
        read -n 1 -s
        echo ""
    fi
    
    # Step 2: Run tests
    echo -e "${BLUE}${BOLD}🧪 Step 2: Run Tests${NC}"
    echo -e "${WHITE}Let's run the tests to make sure everything works with the new versions.${NC}"
    read -p $'\033[1;32m? Run tests now? (Y/n): \033[0m' test_confirm
    
    if [[ ! "$test_confirm" =~ ^[nN]$ ]]; then
        echo ""
        echo -e "${CYAN}Running tests...${NC}"
        
        # Clean before running tests (from project root)
        echo -e "${CYAN}Cleaning project...${NC}"
        
        # Try cleaning specific test project first, then fallback to solution
        if [ -f "$PROJECT_ROOT/test/Test.csproj" ]; then
            echo -e "${CYAN}Running: dotnet clean test/Test.csproj${NC}"
            if (cd "$PROJECT_ROOT" && dotnet clean test/Test.csproj); then
                echo -e "${GREEN}✅ Test project cleaned successfully${NC}"
            else
                echo -e "${YELLOW}⚠️  Test project clean had issues, but continuing...${NC}"
            fi
        fi
        
        # Try to clean common projects
        if [ -f "$PROJECT_ROOT/common/Public/Emarsys.Binding.csproj" ]; then
            echo -e "${CYAN}Running: dotnet clean common/Public/Emarsys.Binding.csproj${NC}"
            if (cd "$PROJECT_ROOT" && dotnet clean common/Public/Emarsys.Binding.csproj); then
                echo -e "${GREEN}✅ Binding project cleaned successfully${NC}"
            else
                echo -e "${YELLOW}⚠️  Binding project clean had issues, but continuing...${NC}"
            fi
        fi
        
        # Restore dependencies before running tests
        echo -e "${CYAN}Restoring dependencies...${NC}"
        if [ -f "$PROJECT_ROOT/test/Test.csproj" ]; then
            echo -e "${CYAN}Running: dotnet restore test/Test.csproj${NC}"
            if (cd "$PROJECT_ROOT" && dotnet restore test/Test.csproj); then
                echo -e "${GREEN}✅ Test dependencies restored successfully${NC}"
            else
                echo -e "${YELLOW}⚠️  Test restore had issues, but continuing...${NC}"
            fi
        fi
        echo ""
        
        # Try to find and run tests (from project root)
        if [ -f "$PROJECT_ROOT/test/Test.csproj" ]; then
            echo -e "${CYAN}Running: dotnet test test/Test.csproj${NC}"
            if (cd "$PROJECT_ROOT" && dotnet test test/Test.csproj); then
                echo -e "${GREEN}✅ All tests passed!${NC}"
            else
                echo -e "${RED}❌ Some tests failed!${NC}"
                echo -e "${YELLOW}You may want to fix the failing tests before committing.${NC}"
                read -p $'\033[1;32m? Continue anyway? (y/N): \033[0m' continue_confirm
                if [[ ! "$continue_confirm" =~ ^[yY]$ ]]; then
                    echo -e "${YELLOW}💡 Fix the tests and run the update script again.${NC}"
                    exit 1
                fi
            fi
        elif [ -f "$PROJECT_ROOT/emarsys.sln" ]; then
            echo -e "${CYAN}Running: dotnet test emarsys.sln${NC}"
            if (cd "$PROJECT_ROOT" && dotnet test emarsys.sln); then
                echo -e "${GREEN}✅ All tests passed!${NC}"
            else
                echo -e "${RED}❌ Some tests failed!${NC}"
                echo -e "${YELLOW}You may want to fix the failing tests before committing.${NC}"
                read -p $'\033[1;32m? Continue anyway? (y/N): \033[0m' continue_confirm
                if [[ ! "$continue_confirm" =~ ^[yY]$ ]]; then
                    echo -e "${YELLOW}💡 Fix the tests and run the update script again.${NC}"
                    exit 1
                fi
            fi
        else
            echo -e "${YELLOW}⚠️  No test project found. Skipping automated tests.${NC}"
            echo -e "${WHITE}Make sure to test your changes manually before committing.${NC}"
        fi
        echo ""
    else
        echo -e "${YELLOW}⚠️  Skipping tests. Remember to test your changes before pushing!${NC}"
        echo ""
    fi
    
    # Step 3: Commit changes
    echo -e "${BLUE}${BOLD}💾 Step 3: Commit Changes${NC}"
    echo -e "${WHITE}Ready to commit these version updates?${NC}"
    
    # Generate commit message
    if [ "$UPDATE_CHANGELOG" = true ]; then
        COMMIT_MSG="chore: release version $NEW_PACKAGE_VERSION"
    else
        COMMIT_MSG="chore: update versions"
    fi
    
    if [ "$CURRENT_ANDROID" != "$NEW_ANDROID_VERSION" ]; then
        COMMIT_MSG="$COMMIT_MSG

- Android SDK: $CURRENT_ANDROID → $NEW_ANDROID_VERSION"
    fi
    if [ "$CURRENT_IOS" != "$NEW_IOS_VERSION" ]; then
        COMMIT_MSG="$COMMIT_MSG

- iOS SDK: $CURRENT_IOS → $NEW_IOS_VERSION (revision: ${NEW_IOS_REVISION:0:8})"
    fi
    if [ "$CURRENT_PACKAGE" != "$NEW_PACKAGE_VERSION" ]; then
        COMMIT_MSG="$COMMIT_MSG

- Package: $CURRENT_PACKAGE → $NEW_PACKAGE_VERSION"
    fi
    
    echo -e "${WHITE}Commit message will be:${NC}"
    echo -e "${CYAN}\"$COMMIT_MSG\"${NC}"
    echo ""
    
    read -p $'\033[1;32m? Commit these changes? (y/N): \033[0m' commit_confirm
    
    if [[ "$commit_confirm" =~ ^[yY]$ ]]; then
        echo ""
        echo -e "${CYAN}Running: git add .${NC}"
        (cd "$PROJECT_ROOT" && git add .)
        
        echo -e "${CYAN}Running: git commit -m \"$COMMIT_MSG\"${NC}"
        (cd "$PROJECT_ROOT" && git commit -m "$COMMIT_MSG")
        
        echo ""
        echo -e "${GREEN}${BOLD}✅ Changes committed successfully!${NC}"
        echo ""
        echo -e "${CYAN}💡 Don't forget to push your changes:${NC}"
        echo -e "${WHITE}   git push${NC}"
    else
        echo -e "${YELLOW}📝 Changes are ready but not committed.${NC}"
        echo -e "${WHITE}When you're ready, run:${NC}"
        echo -e "${CYAN}   git add .${NC}"
        echo -e "${CYAN}   git commit -m \"$COMMIT_MSG\"${NC}"
    fi
    
else
    echo -e "${RED}${BOLD}❌ Some updates failed. Please check the errors above.${NC}"
    exit 1
fi