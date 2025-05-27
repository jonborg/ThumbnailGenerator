package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CharacterInput {
    private String characterName;
    private int alt;
    private boolean flip;
}
