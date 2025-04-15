package enums;

import lombok.Getter;

@Getter
public enum ChosenJsonFieldId {
    TOURNAMENT_THUMBNAIL_CHARACTER_SETTINGS("#fighterImageSettingsFile"),
    TOURNAMENT_TOP8_CHARACTER_SETTINGS("#fighterImageSettingsFileTop8"),
    TOURNAMENT_TOP8_SLOT_SETTINGS("#slotSettingsFileTop8");

    private final String value;

    ChosenJsonFieldId(String value){
        this.value = value;
    }
}
