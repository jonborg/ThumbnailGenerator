VERSION=$1
THUMB="Thumbnail Generator"
EXE_FILE="$THUMB.exe"
README_FILE="README.md"
THUMB_EXAMPLE_FILE="multiThumbnailGenerationExample.txt"
TOP_EXAMPLE_FILE="top8GenerationExample.txt"
ASSET_FOLDER="assets/tournaments"
ASSET_MASK_FOLDER="assets/masks"
ASSET_DOCUMENTATION_FOLDER="assets/documentation"
SETTING_FOLDER="settings"
JRE_FOLDER="jre"  

echo "Creating release $VERSION of Thumbnail Generator"
mvn clean install -Dmaven.test.skip=true

echo "Moving $EXE_FILE to zip"
tar -a -c -f "${THUMB// /}_v${VERSION//./_}.zip" \
  "$EXE_FILE" \
  "$README_FILE" \
  "$THUMB_EXAMPLE_FILE" \
  "$TOP_EXAMPLE_FILE" \
  "$ASSET_FOLDER" \
  "$ASSET_MASK_FOLDER" \
  "$ASSET_DOCUMENTATION_FOLDER" \
  "$SETTING_FOLDER" \
  "$JRE_FOLDER"

echo "Successfully created zip file ${THUMB// /}_v${VERSION//./_}.zip"
