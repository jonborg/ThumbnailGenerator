package enums;

import lombok.Getter;

@Getter
public enum ButtonId {
    TOURNAMENT_WEEKLY_L("#weeklyl"),
    TOURNAMENT_INVICTA("#invicta"),
    SAVE_THUMBNAIL("#saveButton"),
    GENERATE_FROM_FILE("#fromFile");

    private final String value;

    ButtonId(String value){
        this.value = value;
    }
}
