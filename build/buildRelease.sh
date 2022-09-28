VERSION=$1
THUMB="Thumbnail Generator"
EXE_FILE="$THUMB.exe"
README_FILE="README.txt"
EXAMPLE_FILE="multiThumbnailGenerationExample.txt"
ASSET_FOLDER="assets/tournaments"
ASSET_MASK_FOLDER="assets/masks"
SETTING_FOLDER="settings"

echo "Creating release $VERSION of Thumbnail Generator";
mvn clean install

echo "Moving $EXE_FILE to zip"
zip -r "${THUMB// /}_v${VERSION//./_}.zip" "$EXE_FILE" "$README_FILE" "$EXAMPLE_FILE" "$ASSET_FOLDER" "$ASSET_MASK_FOLDER" "$SETTING_FOLDER"
echo "Successfully created zip file ${THUMB// /}_v${VERSION//./_}.zip"
