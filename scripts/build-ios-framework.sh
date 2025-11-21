#!/bin/bash

set -e  # Exit on error

echo "====================================="
echo "Building iOS Native Framework"
echo "====================================="

cd ios/native

xcodebuild -project MauiEmarsys.xcodeproj -scheme MauiEmarsys -configuration Release -sdk iphoneos clean build ARCHS=arm64 ONLY_ACTIVE_ARCH=NO BUILD_DIR=./build
xcodebuild -project MauiEmarsys.xcodeproj -scheme MauiEmarsys -configuration Release -sdk iphonesimulator clean build ARCHS="x86_64 arm64" ONLY_ACTIVE_ARCH=NO BUILD_DIR=./build

rm -rf MauiEmarsysiOS.xcframework
xcodebuild -create-xcframework \
  -framework build/Release-iphoneos/MauiEmarsys.framework \
  -framework build/Release-iphonesimulator/MauiEmarsys.framework \
  -output MauiEmarsysiOS.xcframework

# Find the binary inside the XCFramework
BINARY=$(find MauiEmarsysiOS.xcframework -name "MauiEmarsys" -type f)

# Fail if not found
if [ -z "$BINARY" ]; then
  echo "❌ MauiEmarsys binary not found inside the XCFramework!"
  exit 1
fi

echo "✅ XCFramework built successfully"
file "$BINARY"

# Navigate back to root
cd ../..

echo ""
echo "====================================="
echo "✅ iOS Framework build completed!"
echo "====================================="
