package enums;

import lombok.Getter;

@Getter
public enum ComboBoxId {
    ART_TYPE("#artType"),
    FIGHTER("#fighter"),
    TOURNAMENT_THUMBNAIL_FONT("#font"),
    TOURNAMENT_THUMBNAIL_ART_TYPE("#artTypeThumbnail"),
    TOURNAMENT_TOP8_ART_TYPE("#artTypeTop8");

    private final String value;

    ComboBoxId(String value){
        this.value = value;
    }
}
