#!/bin/bash

# Exit on error
set -e

# === CONFIGURATION ===
OUTPUT_DIR="jre"
MODULES="java.base,java.naming,java.security.sasl,jdk.crypto.ec,javafx.controls,javafx.fxml,javafx.graphics,javafx.swing"

# === VALIDATION ===
if [ -z "$JAVA_HOME" ]; then
  echo "❌ JAVA_HOME is not set in your environment."
  exit 1
fi

if [ -z "$PATH_TO_FX" ]; then
  echo "❌ PATH_TO_FX is not set in your environment (should point to JavaFX SDK /lib)."
  exit 1
fi

if [ ! -d "$PATH_TO_FX" ]; then
  echo "❌ Directory not found: $PATH_TO_FX"
  exit 1
fi

# === INFO ===
echo "Using JAVA_HOME: $JAVA_HOME"
echo "Using PATH_TO_FX: $PATH_TO_FX"
echo "Outputting custom JRE to: $OUTPUT_DIR"
echo "Including modules: $MODULES"

# === JLINK (Windows version) ===
rm -rf "$OUTPUT_DIR"

jlink \
  --module-path "$PATH_TO_FX_MODS;$JAVA_HOME/jmods" \
  --add-modules "$MODULES" \
  --output "$OUTPUT_DIR" \
  --strip-debug \
  --compress=2 \
  --no-header-files \
  --no-man-pages
echo "✅ Custom JRE created at: $OUTPUT_DIR"
