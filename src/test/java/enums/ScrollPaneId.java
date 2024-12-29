package enums;

import lombok.Getter;

@Getter
public enum ScrollPaneId {
    TOURNAMENT_SETTINGS("#tournamentSettings");

    private final String value;

    ScrollPaneId(String value){
        this.value = value;
    }
}
