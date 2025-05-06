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
STARTGG_AUTH_TOKEN_FILE="./settings/startgg/auth-token.txt"
STARTGG_AUTH_TOKEN_BACKUP="auth-token.txt.bak"

echo "Removing auth token"
mv "$STARTGG_AUTH_TOKEN_FILE" "$STARTGG_AUTH_TOKEN_BACKUP"
echo "<set auth token in settings/startgg/auth-token.txt>" > "$STARTGG_AUTH_TOKEN_FILE"

echo "Creating release $VERSION of Thumbnail Generator";
mvn clean install

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

mv "$STARTGG_AUTH_TOKEN_BACKUP" "$STARTGG_AUTH_TOKEN_FILE"
echo "Reverted auth token removal"