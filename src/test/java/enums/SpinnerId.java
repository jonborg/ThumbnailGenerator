package enums;

import lombok.Getter;

@Getter
public enum SpinnerId {
    FIGHTER_ALT("#alt");

    private final String value;

    SpinnerId(String value){
        this.value = value;
    }
}
