package enums;

import lombok.Getter;

@Getter
public enum TextFieldId {
    ROUND("#round"),
    DATE("#date"),
    PLAYER("#player"),
    TOURNAMENT_NAME("#name"),
    TOURNAMENT_ID("#id"),
    TOURNAMENT_THUMBNAIL_FONT_TOP_SIZE("#sizeTop"),
    TOURNAMENT_THUMBNAIL_FONT_TOP_ANGLE("#angleTop"),
    TOURNAMENT_THUMBNAIL_FONT_BOTTOM_SIZE("#sizeBottom"),
    TOURNAMENT_THUMBNAIL_FONT_BOTTOM_ANGLE("#angleBottom"),
    TOURNAMENT_THUMBNAIL_FONT_CONTOUR("#contour"),
    TOURNAMENT_THUMBNAIL_FONT_TOP_LEFT_OFFSET("#downOffsetTopLeft"),
    TOURNAMENT_THUMBNAIL_FONT_TOP_RIGHT_OFFSET("#downOffsetTopRight"),
    TOURNAMENT_THUMBNAIL_FONT_BOTTOM_LEFT_OFFSET("#downOffsetBottomLeft"),
    TOURNAMENT_THUMBNAIL_FONT_BOTTOM_RIGHT_OFFSET("#downOffsetBottomRight");

    private final String value;

    TextFieldId(String value){
        this.value = value;
    }
}
