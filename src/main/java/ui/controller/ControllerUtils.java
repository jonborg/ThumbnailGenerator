package ui.controller;

import fighter.FighterArtType;
import fighter.FighterArtTypeConverter;
import javafx.scene.control.ComboBox;

public class ControllerUtils {

    private ControllerUtils(){
        super();
    }

    public static void initArtDropdown(ComboBox<FighterArtType> artType){
        artType.getItems().addAll(FighterArtType.values());
        artType.setConverter(new FighterArtTypeConverter());
        artType.getSelectionModel().select(FighterArtType.RENDER);
    }
}
