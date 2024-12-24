package enums;

import lombok.Getter;

@Getter
public enum ComboBoxId {
    ART_TYPE("#artType"),
    FIGHTER("#fighter");

    private final String value;

    ComboBoxId(String value){
        this.value = value;
    }
}
