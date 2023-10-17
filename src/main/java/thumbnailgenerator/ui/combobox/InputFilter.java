package thumbnailgenerator.ui.combobox;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

public class InputFilter implements ChangeListener<String> {

    private ComboBox<String> box;
    private FilteredList<String> items;
    private boolean upperCase;
    private int maxLength;
    private String restriction;

    /**
     * @param box
     *            The combo box to whose textProperty this listener is
     *            added.
     * @param items
     *            The {@link FilteredList} containing the items in the list.
     */
    public InputFilter(ComboBox<String> box, FilteredList<String> items, boolean upperCase, int maxLength,
                       String restriction) {
        this.box = box;
        this.items = items;
        this.upperCase = upperCase;
        this.maxLength = maxLength;
        this.restriction = restriction;
    }

    public InputFilter(ComboBox<String> box, FilteredList<String> items, boolean upperCase, int maxLength) {
        this(box, items, upperCase, maxLength, null);
    }

    public InputFilter(ComboBox<String> box, FilteredList<String> items, boolean upperCase) {
        this(box, items, upperCase, -1, null);
    }

    public InputFilter(ComboBox<String> box, FilteredList<String> items) {
        this(box, items, false);
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        StringProperty value = new SimpleStringProperty(newValue);

        // If any item is selected we save that reference.
        String selected = box.getSelectionModel().getSelectedItem() != null
                ? box.getSelectionModel().getSelectedItem() : null;

        String selectedString = null;
        // We save the String of the selected item.
        if (selected != null) {
            selectedString = (String) selected;
        }

        if (upperCase) {
            value.set(value.get().toUpperCase());
        }

        if (maxLength >= 0 && value.get().length() > maxLength) {
            value.set(oldValue);
        }

        if (restriction != null) {
            if (!value.get().matches(restriction + "*")) {
                value.set(oldValue);
            }
        }

        // If an item is selected and the value in the editor is the same
        // as the selected item we don't filter the list.
        if (selected != null && value.get().equals(selectedString)) {
            // This will place the caret at the end of the string when
            // something is selected.
            Platform.runLater(() -> box.getEditor().end());
        } else {
            items.setPredicate(item -> {
                String itemString = item;
                if (itemString.toUpperCase().contains(value.get().toUpperCase())) {
                    return true;
                } else {
                    return false;
                }
            });
        }

        // If the popup isn't already showing we show it.
        if (!box.isShowing()) {
            // If the new value is empty we don't want to show the popup,
            // since
            // this will happen when the combo box gets manually reset.
            if (!newValue.isEmpty() && box.isFocused()) {
                box.show();
            }
        }
        // If it is showing and there's only one item in the popup, which is
        // an
        // exact match to the text, we hide the dropdown.
        else {
            if (items.size() == 1) {
                // We need to get the String differently depending on the
                // nature
                // of the object.
                String item = items.get(0);

                // To get the value we want to compare with the written
                // value, we need to crop the value according to the current
                // selectionCrop.
                String comparableItem = item;

                if (value.get().equals(comparableItem)) {
                    Platform.runLater(() -> box.hide());
                }
            }
        }

        box.getEditor().setText(value.get());
    }
}

