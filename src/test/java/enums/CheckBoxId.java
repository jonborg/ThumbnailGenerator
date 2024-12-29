package enums;

import lombok.Getter;

@Getter
public enum CheckBoxId {
    FLIP_FIGHTER("#flip"),
    SAVE_LOCALLY("#saveLocally"),
    TOURNAMENT_FONT_BOLD("#bold"),
    TOURNAMENT_FONT_ITALIC("#italic"),
    TOURNAMENT_FONT_SHADOW("#shadow"),

    ;

    private final String value;

    CheckBoxId(String value){
        this.value = value;
    }
}
