package enums;

import lombok.Getter;

@Getter
public enum MenuId {
    EDIT("#menuEdit"),
    CREATE_TOURNAMENT("#menuCreateTournament"),
    COPY_TOURNAMENT("#menuCopyTournament"),
    EDIT_TOURNAMENT("#menuEditTournament"),
    DELETE_TOURNAMENT("#menuDeleteTournament"),
    COPY_INVICTA("#menuCopy_invicta"),
    EDIT_INVICTA("#menuEdit_invicta"),
    EDIT_S_TIER("#menuEdit_stier"),
    EDIT_WEEKLY_L("#menuEdit_weeklyl"),
    DELETE_INVICTA("#menuDelete_invicta");

    private final String value;

    MenuId(String value){
        this.value = value;
    }
}
