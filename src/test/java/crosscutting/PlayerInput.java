package crosscutting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerInput {
    private String playerName;
    private String characterName;
    private int alt;
    private boolean flip;

}
