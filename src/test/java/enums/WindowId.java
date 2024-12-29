package enums;

import lombok.Getter;

@Getter
public enum WindowId {
    MAIN("mainWindow"),
    CREATE_TOURNAMENT("createTournamentWindow"),
    EDIT_TOURNAMENT("editTournamentWindow"),
    STARTGG("startGGWindow");

    private final String value;

    WindowId(String value){
        this.value = value;
    }
}
