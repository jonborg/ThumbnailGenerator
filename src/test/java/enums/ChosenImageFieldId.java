package enums;

import lombok.Getter;

@Getter
public enum ChosenImageFieldId {
    TOURNAMENT_LOGO("#logo"),
    TOURNAMENT_THUMBNAIL_FOREGROUND("#foreground"),
    TOURNAMENT_THUMBNAIL_BACKGROUND("#background"),
    TOURNAMENT_THUMBNAIL_CHARACTER_SETTINGS("#fighterImageSettingsFile"),
    TOURNAMENT_TOP8_FOREGROUND("#foregroundTop8"),
    TOURNAMENT_TOP8_BACKGROUND("#backgroundTop8"),
    TOURNAMENT_TOP8_CHARACTER_SETTINGS("#fighterImageSettingsFileTop8");

    private final String value;

    ChosenImageFieldId(String value){
        this.value = value;
    }
}
