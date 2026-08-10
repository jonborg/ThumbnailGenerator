package enums;

import lombok.Getter;

@Getter
public enum ButtonId {
    TOURNAMENT_WEEKLY_L("#weeklyl"),
    TOURNAMENT_INVICTA("#invicta"),
    ADD_REMOVE_CHARACTER_2("#addRemoveCharacter2"),
    ADD_REMOVE_CHARACTER_3("#addRemoveCharacter3"),
    ADD_REMOVE_CHARACTER_4("#addRemoveCharacter4"),
    ADD_REMOVE_CHARACTER_5("#addRemoveCharacter5"),
    SAVE_THUMBNAIL("#saveButton"),
    GENERATE_FROM_FILE("#fromFile"),
    SAVE_TOURNAMENT("#saveTournamentButton"),
    CANCEL_TOURNAMENT("#cancelButton"),
    TOURNAMENT_THUMBNAIL_PREVIEW("#previewButton");

    private final String value;

    ButtonId(String value){
        this.value = value;
    }
}
