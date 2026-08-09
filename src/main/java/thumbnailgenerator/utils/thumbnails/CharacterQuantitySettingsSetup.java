package thumbnailgenerator.utils.thumbnails;

import thumbnailgenerator.dto.CharacterQuantitySettings;
import thumbnailgenerator.enums.thumbnails.CharacterQuantity;

import java.util.ArrayList;
import java.util.Arrays;

public class CharacterQuantitySettingsSetup {

    private static String thumbnailMaskPath(CharacterQuantity characterQuantity, String fileName) {
        return "assets/masks/thumbnails/" + characterQuantity.name() + "/" + fileName;
    }

    public static CharacterQuantitySettings getCharacterQuantitySettingsForTwo(
            int port
    ) {
        if (port == 1){
            return getPlayer1CharacterQuantitySettingsForTwo();
        } else {
            return getPlayer2CharacterQuantitySettingsForTwo();
        }
    }

    public static CharacterQuantitySettings getCharacterQuantitySettingsForThree(
            int port,
            Integer thumbnailWidth
    ) {
        if (port == 1){
            return getPlayer1CharacterQuantitySettingsForThree(thumbnailWidth);
        } else {
            return getPlayer2CharacterQuantitySettingsForThree(thumbnailWidth);
        }
    }

    public static CharacterQuantitySettings getCharacterQuantitySettingsForFour(
            int port,
            Integer thumbnailWidth,
            Integer thumbnailHeight
    ) {
        if (port == 1){
            return getPlayer1CharacterQuantitySettingsForFour(thumbnailWidth, thumbnailHeight);
        } else {
            return getPlayer2CharacterQuantitySettingsForFour(thumbnailWidth, thumbnailHeight);
        }
    }

    public static CharacterQuantitySettings getCharacterQuantitySettingsForFive(
            int port,
            Integer thumbnailWidth,
            Integer thumbnailHeight
    ) {
        if (port == 1){
            return getPlayer1CharacterQuantitySettingsForFive(thumbnailWidth, thumbnailHeight);
        } else {
            return getPlayer2CharacterQuantitySettingsForFive(thumbnailWidth, thumbnailHeight);
        }
    }

    private static CharacterQuantitySettings getPlayer1CharacterQuantitySettingsForTwo(){
        var masksFiles = new ArrayList<>(Arrays.asList(
                thumbnailMaskPath(CharacterQuantity.TWO,"char2.png"),
                thumbnailMaskPath(CharacterQuantity.TWO,"char1.png")
        ));
        var characterOrder = new ArrayList<>(Arrays.asList(1,0));
        var characterExtraOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {-100, -50},
                new Integer[] {100, 50}
        ));
        var maskOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0, 0},
                new Integer[] {0, 0}
        ));

        return new CharacterQuantitySettings(
                2,
                0.7f,
                masksFiles,
                characterOrder,
                characterExtraOffsets,
                maskOffsets
        );
    }

    private static CharacterQuantitySettings getPlayer2CharacterQuantitySettingsForTwo(){
        var masksFiles = new ArrayList<>(Arrays.asList(
                thumbnailMaskPath(CharacterQuantity.TWO,"char4.png"),
                thumbnailMaskPath(CharacterQuantity.TWO,"char3.png")
        ));
        var characterOrder = new ArrayList<>(Arrays.asList(1,0));
        var characterExtraOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {100, -50},
                new Integer[] {-100, 50}
        ));
        var maskOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0, 0},
                new Integer[] {0, 0}
        ));

        return new CharacterQuantitySettings(
                2,
                0.7f,
                masksFiles,
                characterOrder,
                characterExtraOffsets,
                maskOffsets
        );
    }

    private static CharacterQuantitySettings getPlayer1CharacterQuantitySettingsForThree(
            Integer thumbnailWidth
    ){
        var masksFiles = new ArrayList<>(Arrays.asList(
                thumbnailMaskPath(CharacterQuantity.THREE,"charTop.png"),
                thumbnailMaskPath(CharacterQuantity.THREE,"charLeft.png"),
                thumbnailMaskPath(CharacterQuantity.THREE,"charRight.png")
        ));
        var characterOrder = new ArrayList<>(Arrays.asList(0,2,1));
        var characterExtraOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0,-100},
                new Integer[] {-200, 50},
                new Integer[] {-100, 50}
        ));
        var maskOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0, 0},
                new Integer[] {0, 0},
                new Integer[] {thumbnailWidth/4, 0}
        ));

        return new CharacterQuantitySettings(
                3,
                0.6f,
                masksFiles,
                characterOrder,
                characterExtraOffsets,
                maskOffsets
        );
    }

    private static CharacterQuantitySettings getPlayer2CharacterQuantitySettingsForThree(
            Integer thumbnailWidth
    ){
        var masksFiles = new ArrayList<>(Arrays.asList(
                thumbnailMaskPath(CharacterQuantity.THREE,"charTop.png"),
                thumbnailMaskPath(CharacterQuantity.THREE,"charRight.png"),
                thumbnailMaskPath(CharacterQuantity.THREE,"charLeft.png")

        ));
        var characterOrder = new ArrayList<>(Arrays.asList(0,2,1));
        var characterExtraOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0,-100},
                new Integer[] {-100, 50},
                new Integer[] {-200, 50}
        ));
        var maskOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0, 0},
                new Integer[] {thumbnailWidth/4, 0},
                new Integer[] {0, 0}
        ));

        return new CharacterQuantitySettings(
                3,
                0.6f,
                masksFiles,
                characterOrder,
                characterExtraOffsets,
                maskOffsets
        );
    }

    private static CharacterQuantitySettings getPlayer1CharacterQuantitySettingsForFour(
            Integer thumbnailWidth,
            Integer thumbnailHeight
    ){
        var masksFiles = new ArrayList<>(Arrays.asList(
                thumbnailMaskPath(CharacterQuantity.FOUR,"charTop.png"),
                thumbnailMaskPath(CharacterQuantity.FOUR,"charLeft.png"),
                thumbnailMaskPath(CharacterQuantity.FOUR,"charRight.png"),
                thumbnailMaskPath(CharacterQuantity.FOUR,"charBottom.png")
        ));
        var characterOrder = new ArrayList<>(Arrays.asList(0,3,1,2));
        var characterExtraOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0,-150},
                new Integer[] {-200, -50},
                new Integer[] {-100, -50},
                new Integer[] {0, -200}
        ));
        var maskOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0, 0},
                new Integer[] {0, 40},
                new Integer[] {thumbnailWidth/4, 40},
                new Integer[] {0, thumbnailHeight/2}
        ));

        return new CharacterQuantitySettings(
                4,
                0.5f,
                masksFiles,
                characterOrder,
                characterExtraOffsets,
                maskOffsets
        );
    }

    private static CharacterQuantitySettings getPlayer2CharacterQuantitySettingsForFour(
            Integer thumbnailWidth,
            Integer thumbnailHeight
    ) {
        var masksFiles = new ArrayList<>(Arrays.asList(
                thumbnailMaskPath(CharacterQuantity.FOUR, "charTop.png"),
                thumbnailMaskPath(CharacterQuantity.FOUR, "charRight.png"),
                thumbnailMaskPath(CharacterQuantity.FOUR, "charLeft.png"),
                thumbnailMaskPath(CharacterQuantity.FOUR, "charBottom.png")
        ));
        var characterOrder = new ArrayList<>(Arrays.asList(0, 1, 3, 2));
        var characterExtraOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0, -150},
                new Integer[] {-100, -50},
                new Integer[] {-200, -50},
                new Integer[] {20, -200}
        ));
        var maskOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {0, 0},
                new Integer[] {thumbnailWidth / 4, 40},
                new Integer[] {0, 40},
                new Integer[] {0, thumbnailHeight / 2}
        ));

        return new CharacterQuantitySettings(
                4,
                0.5f,
                masksFiles,
                characterOrder,
                characterExtraOffsets,
                maskOffsets
        );
    }

    private static CharacterQuantitySettings getPlayer1CharacterQuantitySettingsForFive(
            Integer thumbnailWidth,
            Integer thumbnailHeight
    ){
        var masksFiles = new ArrayList<>(Arrays.asList(
                thumbnailMaskPath(CharacterQuantity.FIVE,"charTop.png"),
                thumbnailMaskPath(CharacterQuantity.FIVE,"charLeft.png"),
                thumbnailMaskPath(CharacterQuantity.FIVE,"charRight.png"),
                thumbnailMaskPath(CharacterQuantity.FIVE,"charBottomLeft.png"),
                thumbnailMaskPath(CharacterQuantity.FIVE,"charBottomRight.png")
        ));
        var characterOrder = new ArrayList<>(Arrays.asList(0,4,1,3,2));
        var characterExtraOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {-42,-150},
                new Integer[] {-200, -50},
                new Integer[] {-100, -50},
                new Integer[] {-160, -220},
                new Integer[] {-160, -220}
        ));
        var maskOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {42, 0},
                new Integer[] {0, 0},
                new Integer[] {thumbnailWidth/4, 0},
                new Integer[] {0, thumbnailHeight-330},
                new Integer[] {thumbnailWidth/4, thumbnailHeight-330}
        ));

        return new CharacterQuantitySettings(
                5,
                0.4f,
                masksFiles,
                characterOrder,
                characterExtraOffsets,
                maskOffsets
        );
    }

    private static CharacterQuantitySettings getPlayer2CharacterQuantitySettingsForFive(
            Integer thumbnailWidth,
            Integer thumbnailHeight
    ){
        var masksFiles = new ArrayList<>(Arrays.asList(
                thumbnailMaskPath(CharacterQuantity.FIVE,"charTop.png"),
                thumbnailMaskPath(CharacterQuantity.FIVE,"charRight.png"),
                thumbnailMaskPath(CharacterQuantity.FIVE,"charLeft.png"),
                thumbnailMaskPath(CharacterQuantity.FIVE,"charBottomRight.png"),
                thumbnailMaskPath(CharacterQuantity.FIVE,"charBottomLeft.png")
        ));
        var characterOrder = new ArrayList<>(Arrays.asList(0,4,1,3,2));
        var characterExtraOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {-42,-150},
                new Integer[] {-100, -50},
                new Integer[] {-200, -50},
                new Integer[] {-160, -220},
                new Integer[] {-160, -220}
        ));
        var maskOffsets = new ArrayList<>(Arrays.asList(
                new Integer[] {42, 0},
                new Integer[] {thumbnailWidth/4, 0},
                new Integer[] {0, 0},
                new Integer[] {thumbnailWidth/4, thumbnailHeight-330},
                new Integer[] {0, thumbnailHeight-330}
        ));

        return new CharacterQuantitySettings(
                5,
                0.4f,
                masksFiles,
                characterOrder,
                characterExtraOffsets,
                maskOffsets
        );
    }
}
