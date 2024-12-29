package enums;

import lombok.Getter;

@Getter
public enum ChosenJsonFieldId {
    TOURNAMENT_TOP8_SLOT_SETTINGS("#slotSettingsFileTop8");

    private final String value;

    ChosenJsonFieldId(String value){
        this.value = value;
    }
}
