package enums;

import lombok.Getter;

@Getter
public enum TextFieldId {
    ROUND("#round"),
    DATE("#date"),
    PLAYER("#player");

    private final String value;

    TextFieldId(String value){
        this.value = value;
    }
}
