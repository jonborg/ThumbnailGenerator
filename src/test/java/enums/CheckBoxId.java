package enums;

import lombok.Getter;

@Getter
public enum CheckBoxId {
    FLIP_FIGHTER("#flip"),
    SAVE_LOCALLY("#saveLocally");

    private final String value;

    CheckBoxId(String value){
        this.value = value;
    }
}
