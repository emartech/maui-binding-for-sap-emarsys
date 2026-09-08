#!/bin/bash
# Asserts that the package version is the same in all three authoritative sources.
# Exits 1 with a clear message if they disagree; exits 0 when aligned.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

GLOBAL_USING="$PROJECT_ROOT/common/Public/GlobalUsing.cs"
NUSPEC="$PROJECT_ROOT/common/Public/Emarsys.Binding.nuspec"
CHANGELOG="$PROJECT_ROOT/CHANGELOG.md"

for f in "$GLOBAL_USING" "$NUSPEC" "$CHANGELOG"; do
  if [ ! -f "$f" ]; then
    echo "ERROR: required file not found: $f" >&2
    exit 1
  fi
done

VERSION_CS=$(grep -o 'packageVersion = "[^"]*"' "$GLOBAL_USING" | sed 's/packageVersion = "\(.*\)"/\1/')
VERSION_NUSPEC=$(grep -o '<version>[^<]*</version>' "$NUSPEC" | sed 's/<version>\(.*\)<\/version>/\1/')
VERSION_CHANGELOG=$(grep -E "^# [0-9]+\.[0-9]+\.[0-9]+" "$CHANGELOG" | head -1 | grep -oE "[0-9]+\.[0-9]+\.[0-9]+")

if [ -z "$VERSION_CS" ]; then
  echo "ERROR: could not extract version from $GLOBAL_USING" >&2; exit 1
fi
if [ -z "$VERSION_NUSPEC" ]; then
  echo "ERROR: could not extract version from $NUSPEC" >&2; exit 1
fi
if [ -z "$VERSION_CHANGELOG" ]; then
  echo "ERROR: could not extract version from $CHANGELOG" >&2; exit 1
fi

echo "Version sources:"
echo "  GlobalUsing.cs         : $VERSION_CS"
echo "  Emarsys.Binding.nuspec : $VERSION_NUSPEC"
echo "  CHANGELOG.md           : $VERSION_CHANGELOG"

if [ "$VERSION_CS" != "$VERSION_NUSPEC" ] || [ "$VERSION_CS" != "$VERSION_CHANGELOG" ]; then
  echo ""
  echo "ERROR: version mismatch detected." >&2
  echo "All three sources must agree before a release can be tagged." >&2
  echo "Run ./update to update them together, or fix the diverging file manually." >&2
  exit 1
fi

echo ""
echo "OK: all three sources agree on version $VERSION_CS"
