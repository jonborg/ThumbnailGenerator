package thumbnailgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class CharacterQuantitySettings {
    private int characterQuantity;
    private float characterScale;
    private ArrayList<String> maskFiles;
    private ArrayList<Integer> characterOrder;
    private ArrayList<Integer[]> characterExtraOffSets;
    private ArrayList<Integer[]> maskOffSets;
}
