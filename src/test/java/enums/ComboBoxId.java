package enums;

import lombok.Getter;

@Getter
public enum ComboBoxId {
    GAME("gameComboBox"),
    ART_TYPE("#artTypeComboBox"),
    CHARACTER_1("#character1"),
    CHARACTER_2("#character2"),
    TOURNAMENT_GAME("#tournamentGame"),
    TOURNAMENT_THUMBNAIL_FONT("#font"),
    TOURNAMENT_THUMBNAIL_ART_TYPE("#artTypeThumbnail"),
    TOURNAMENT_TOP8_ART_TYPE("#artTypeTop8");

    private final String value;

    ComboBoxId(String value){
        this.value = value;
    }
}
