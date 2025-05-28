package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PlayerInput {
    private String playerName;
    List<CharacterInput> characterInputList;
}
