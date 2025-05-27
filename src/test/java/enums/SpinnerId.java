package enums;

import lombok.Getter;

@Getter
public enum SpinnerId {
    ALT_CHARACTER_1("#alt1"),
    ALT_CHARACTER_2("#alt2");

    private final String value;

    SpinnerId(String value){
        this.value = value;
    }
}
