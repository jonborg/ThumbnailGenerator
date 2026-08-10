package enums;

import lombok.Getter;

@Getter
public enum SpinnerId {
    ALT_CHARACTER_1("#alt1"),
    ALT_CHARACTER_2("#alt2"),
    ALT_CHARACTER_3("#alt3"),
    ALT_CHARACTER_4("#alt4"),
    ALT_CHARACTER_5("#alt5");

    private final String value;

    SpinnerId(String value){
        this.value = value;
    }
}
