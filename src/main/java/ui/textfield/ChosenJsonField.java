package ui.textfield;

import java.util.ArrayList;

public class ChosenJsonField extends ChosenImageField {

    public ChosenJsonField() {
        super();
        description = "JSON Files";
        extensions = new ArrayList<>();
        extensions.add("*.json");

        initComponents();
    }
}